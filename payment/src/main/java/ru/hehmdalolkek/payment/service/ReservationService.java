package ru.hehmdalolkek.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.hehmdalolkek.basemodels.dto.NotificationDto;
import ru.hehmdalolkek.basemodels.dto.ReservationDto;
import ru.hehmdalolkek.basemodels.dto.Status;
import ru.hehmdalolkek.payment.config.PaymentAppConfiguration;
import ru.hehmdalolkek.payment.mapper.ReservationMapper;
import ru.hehmdalolkek.payment.model.Reservation;

import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationService {

    private final RabbitTemplate rabbitTemplate;

    private final ReservationMapper reservationMapper;

    private final PaymentAppConfiguration config;

    @RabbitListener(queues = "#{paymentAppConfiguration.getPaymentQueueName()}")
    private void payReservation(ReservationDto reservationDto) {
        Reservation reservation = this.reservationMapper.ReservationDtoToReservation(reservationDto);
        boolean isSuccess = this.proceedPay(reservation);
        if (isSuccess) {
            String message = "Reservation successfully paid.\n" +
                    "Date: " + reservation.getDate() + ".\n" +
                    "Time: " + reservation.getTime() + ".\n" +
                    "Price: $" + reservation.getPrice() + ".";
            this.sendNotification(
                    reservation,
                    false,
                    message,
                    Status.PAYMENT
            );
        } else {
            String message = "An error has occurred. Reservation not paid.\n" +
                    "Date: " + reservation.getDate() + ".\n" +
                    "Time: " + reservation.getTime() + ".\n" +
                    "Price: $" + reservation.getPrice() + ".";
            this.sendNotification(
                    reservation,
                    true,
                    message,
                    Status.PAYMENT
            );
        }
    }

    public void sendNotification(Reservation reservation, boolean isError, String message, Status status) {
        NotificationDto notificationDto = NotificationDto.builder()
                .reservation(this.reservationMapper.ReservationToReservationDto(reservation))
                .error(isError)
                .message(message)
                .status(status)
                .build();
        this.rabbitTemplate.convertAndSend(
                this.config.getPaymentDirectExchangeName(),
                this.config.getPaymentNotificationRoutingKeyName(),
                notificationDto);
        log.info("Notification - {} sent to {}.", notificationDto, this.config.getNotificationQueueName());
    }

    public boolean proceedPay(Reservation reservation) {
        // Making a payment
        Random random = new Random();
        int randomInt = random.nextInt(1, 101);
        return randomInt > 5;
    }

}
