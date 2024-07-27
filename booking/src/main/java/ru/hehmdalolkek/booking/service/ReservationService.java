package ru.hehmdalolkek.booking.service;

import ru.hehmdalolkek.basemodels.dto.ReservationDto;
import ru.hehmdalolkek.basemodels.dto.Status;
import ru.hehmdalolkek.booking.model.Reservation;

public interface ReservationService {

    void receiveReservation(ReservationDto reservationDto);

    boolean bookReservation(Reservation reservation);

    void payReservation(ReservationDto reservationDto);

    void sendNotification(Reservation reservation, boolean isError, String message, Status status);

}
