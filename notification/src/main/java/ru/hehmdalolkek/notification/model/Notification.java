package ru.hehmdalolkek.notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    private Status status;

    private String message;

    private boolean error;

    private Reservation reservation;

}
