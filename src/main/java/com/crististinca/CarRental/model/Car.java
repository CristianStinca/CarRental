package com.crististinca.CarRental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Brand name cannot be empty.")
    @Size(min = 2, max = 250)
    private String brand;

    @NotEmpty(message = "Model name cannot be empty.")
    @Size(min = 2, max = 250)
    private String model;

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Rents> rentals;
}
