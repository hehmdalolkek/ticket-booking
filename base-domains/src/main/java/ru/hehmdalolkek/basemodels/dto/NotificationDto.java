package ru.hehmdalolkek.basemodels.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    Status status;

    String message;

    boolean error;

    ReservationDto reservationDto;

}
