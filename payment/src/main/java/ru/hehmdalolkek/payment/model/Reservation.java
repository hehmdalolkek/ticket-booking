package ru.hehmdalolkek.payment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    private UUID id;

    private String email;

    private LocalDate date;

    private LocalTime time;

    private double price;

}
