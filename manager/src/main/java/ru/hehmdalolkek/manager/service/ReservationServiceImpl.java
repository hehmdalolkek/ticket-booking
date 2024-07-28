package ru.hehmdalolkek.manager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.hehmdalolkek.basemodels.dto.NotificationDto;
import ru.hehmdalolkek.basemodels.dto.ReservationDto;
import ru.hehmdalolkek.basemodels.dto.Status;
import ru.hehmdalolkek.manager.config.ManagerAppConfiguration;
import ru.hehmdalolkek.manager.mapper.ReservationMapper;
import ru.hehmdalolkek.manager.model.Reservation;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final RabbitTemplate rabbitTemplate;

    private final ReservationMapper reservationMapper;

    private final ManagerAppConfiguration config;

    @Override
    public void bookReservation(ReservationDto reservationDto) {
        this.rabbitTemplate.convertAndSend(
                this.config.getManagerDirectExchangeName(),
                this.config.getManagerBookingRoutingKeyName(),
                reservationDto);
        log.info("Reservation - {} sent to {}.", reservationDto, this.config.getBookingQueueName());
    }

    @Override
    public void sendNotification(ReservationDto reservationDto) {
        String message = "Reservation accepted for processing.\n" +
                "Date: " + reservationDto.getDate() + ".\n" +
                "Time: " + reservationDto.getTime() + ".\n" +
                "Price: $" + reservationDto.getPrice() + ".";
        NotificationDto notificationDto = NotificationDto.builder()
                .reservation(reservationDto)
                .status(Status.MANAGEMENT)
                .message(message)
                .error(false)
                .build();
        this.rabbitTemplate.convertAndSend(
                this.config.getManagerDirectExchangeName(),
                this.config.getManagerNotificationRoutingKeyName(),
                notificationDto);
        log.info("Notification - {} sent to {}.", notificationDto, this.config.getNotificationQueueName());
    }

    @Override
    public ReservationDto processingReservation(ReservationDto reservationDto) {
        Reservation reservation = this.reservationMapper.ReservationDtoToReservation(reservationDto);
        reservation.setId(UUID.randomUUID());
        return this.reservationMapper.ReservationToReservationDto(reservation);
    }

}
