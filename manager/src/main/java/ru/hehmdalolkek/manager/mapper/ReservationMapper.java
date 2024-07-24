package ru.hehmdalolkek.manager.mapper;

import org.mapstruct.Mapper;
import ru.hehmdalolkek.basemodels.dto.ReservationDto;
import ru.hehmdalolkek.manager.model.Reservation;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    ReservationDto ReservationToReservationDto(Reservation reservation);

    Reservation ReservationDtoToReservation(ReservationDto reservationDto);

}
