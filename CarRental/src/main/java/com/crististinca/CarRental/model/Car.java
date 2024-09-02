package com.crististinca.CarRental.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class Car {
    private Long id;

    @NotEmpty(message = "brand name cannot be empty.")
    @Size(min = 2, max = 250)
    private String brand;

    @NotEmpty(message = "model name cannot be empty.")
    @Size(min = 2, max = 250)
    private String model;

    @NotNull(message = "car should be either active or inactive.")
    private Boolean isActive;

    @Positive(message = "price should be positive.")
    @NotNull(message = "price should be set.")
    private Integer price;

    private Set<Rents> rentals;

    private byte[] imageData;
}
