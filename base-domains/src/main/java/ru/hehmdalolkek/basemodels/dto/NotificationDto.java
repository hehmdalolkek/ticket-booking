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

    private Status status;

    private String message;

    private boolean error;

    private ReservationDto reservation;

}
