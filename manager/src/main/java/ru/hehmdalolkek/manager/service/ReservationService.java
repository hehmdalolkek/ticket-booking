package ru.hehmdalolkek.manager.service;

import ru.hehmdalolkek.basemodels.dto.ReservationDto;

public interface ReservationService {

    void bookReservation(ReservationDto reservationDto);

    void sendNotification(ReservationDto reservationDto);

}