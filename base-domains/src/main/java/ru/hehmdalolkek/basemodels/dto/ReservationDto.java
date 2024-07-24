package ru.hehmdalolkek.basemodels.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    private UUID id;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime time;

    @NotNull
    private double price;

}