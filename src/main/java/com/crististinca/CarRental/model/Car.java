package com.crististinca.CarRental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Car {
    @Id
    @SequenceGenerator(
            name = "car_sequence",
            sequenceName = "car_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "car_sequence"
    )
    private Long id;

    @NotEmpty(message = "Brand name cannot be empty.")
    @Size(min = 2, max = 250)
    private String brand;

    @NotEmpty(message = "Model name cannot be empty.")
    @Size(min = 2, max = 250)
    private String model;

    private Boolean reserved;

    private Long reserved_by;
}
