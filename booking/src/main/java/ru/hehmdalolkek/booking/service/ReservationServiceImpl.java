package ru.hehmdalolkek.booking.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hehmdalolkek.basemodels.dto.NotificationDto;
import ru.hehmdalolkek.basemodels.dto.ReservationDto;
import ru.hehmdalolkek.basemodels.dto.Status;
import ru.hehmdalolkek.booking.config.BookingAppConfiguration;
import ru.hehmdalolkek.booking.mapper.ReservationMapper;
import ru.hehmdalolkek.booking.model.Reservation;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final ReservationMapper reservationMapper;

    private final RabbitTemplate rabbitTemplate;

    private final BookingAppConfiguration config;

    @Transactional
    @RabbitListener(queues = "#{bookingAppConfiguration.getBookingQueueName()}")
    public void receiveReservation(ReservationDto reservationDto) {
        Reservation reservation = this.reservationMapper.ReservationDtoToReservation(reservationDto);
        boolean isSuccess = this.bookReservation(reservation);
        if (isSuccess) {
            String message = "Reservation successfully booked.\n" +
                    "Date: " + reservation.getDate() + ".\n" +
                    "Time: " + reservation.getTime() + ".\n" +
                    "Price: $" + reservation.getPrice() + ".";
            this.sendNotification(
                    reservation,
                    false,
                    message,
                    Status.BOOKING
            );
            this.payReservation(this.reservationMapper.ReservationToReservationDto(reservation));
        } else {
            String message = "An error has occurred. Reservation not booked.\n" +
                    "Date: " + reservation.getDate() + ".\n" +
                    "Time: " + reservation.getTime() + ".\n" +
                    "Price: $" + reservation.getPrice() + ".";
            this.sendNotification(
                    reservation,
                    true,
                    message,
                    Status.BOOKING
            );
        }
    }

    @Transactional
    public boolean bookReservation(Reservation reservation) {
        try {
            this.entityManager.merge(reservation);
            return true;
        } catch (PersistenceException exception) {
            return false;
        }
    }

    public void payReservation(ReservationDto reservationDto) {
        this.rabbitTemplate.convertAndSend(
                this.config.getBookingDirectExchangeName(),
                this.config.getBookingPaymentRoutingKey(),
                reservationDto);
        log.info("Reservation - {} sent to {}.", reservationDto, this.config.getPaymentQueueName());
    }

    public void sendNotification(Reservation reservation, boolean isError, String message, Status status) {
        NotificationDto notificationDto = NotificationDto.builder()
                .reservation(this.reservationMapper.ReservationToReservationDto(reservation))
                .error(isError)
                .message(message)
                .status(status)
                .build();
        this.rabbitTemplate.convertAndSend(
                this.config.getBookingDirectExchangeName(),
                this.config.getBookingNotificationRoutingKey(),
                notificationDto);
        log.info("Notification - {} sent to {}.", notificationDto, this.config.getNotificationQueueName());
    }

}