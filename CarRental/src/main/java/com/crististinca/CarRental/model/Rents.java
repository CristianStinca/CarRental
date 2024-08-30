package com.crististinca.CarRental.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rents {
    private Long id;

    private Car car;

    private Client client;

    private LocalDate rentalDateStart;

    private LocalDate rentalDateEnd;
}
