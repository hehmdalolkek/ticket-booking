package ru.hehmdalolkek.basemodels.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto implements Serializable {

    UUID id;

    String email;

    LocalDate date;

    LocalTime time;

    double price;

}