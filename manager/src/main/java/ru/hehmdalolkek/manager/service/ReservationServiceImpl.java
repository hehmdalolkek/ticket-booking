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

    @Override
    public void bookReservation(ReservationDto reservationDto) {
        this.rabbitTemplate.convertAndSend(
                ManagerAppConfiguration.MANAGER_EXCHANGE_NAME,
                ManagerAppConfiguration.BOOKING_ROUTING_KEY,
                reservationDto);
        log.info("Reservation - {} sent to {}.", reservationDto, ManagerAppConfiguration.BOOKING_QUEUE_NAME);
    }

    @Override
    public void sendNotification(ReservationDto reservationDto) {
        NotificationDto notificationDto = NotificationDto.builder()
                .reservation(reservationDto)
                .status(Status.MANAGEMENT)
                .message("Reservation accepted for processing.")
                .error(false)
                .build();
        this.rabbitTemplate.convertAndSend(
                ManagerAppConfiguration.MANAGER_EXCHANGE_NAME,
                ManagerAppConfiguration.NOTIFICATION_ROUTING_KEY,
                notificationDto);
        log.info("Notification - {} sent to {}.", notificationDto, ManagerAppConfiguration.NOTIFICATION_QUEUE_NAME);
    }

    @Override
    public ReservationDto processingReservation(ReservationDto reservationDto) {
        Reservation reservation = this.reservationMapper.ReservationDtoToReservation(reservationDto);
        reservation.setId(UUID.randomUUID());
        return this.reservationMapper.ReservationToReservationDto(reservation);
    }

}
