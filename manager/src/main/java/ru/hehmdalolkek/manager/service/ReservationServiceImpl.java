package ru.hehmdalolkek.manager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.hehmdalolkek.basemodels.dto.NotificationDto;
import ru.hehmdalolkek.basemodels.dto.ReservationDto;
import ru.hehmdalolkek.basemodels.dto.Status;
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
        this.rabbitTemplate.convertAndSend("manager.booking", reservationDto);
        log.info("Reservation - {} sent to manager.booking queue.", reservationDto);
    }

    @Override
    public void sendNotification(ReservationDto reservationDto) {
        NotificationDto notificationDto = NotificationDto.builder()
                .reservation(reservationDto)
                .status(Status.MANAGEMENT)
                .message("Reservation accepted for processing.")
                .error(false)
                .build();
        this.rabbitTemplate.convertAndSend("manager.notification", notificationDto);
        log.info("Notification - {} sent to manager.notification queue.", notificationDto);
    }

    @Override
    public ReservationDto processingReservation(ReservationDto reservationDto) {
        Reservation reservation = this.reservationMapper.ReservationDtoToReservation(reservationDto);
        reservation.setId(UUID.randomUUID());
        return this.reservationMapper.ReservationToReservationDto(reservation);
    }

}
