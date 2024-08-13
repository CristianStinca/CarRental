package com.crististinca.CarRental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarbinaryJdbcType;

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

    @NotNull(message = "Car should be either active or inactive.")
    private Boolean isActive;

    @Positive(message = "Price should be set and positive.")
    private Integer price;

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Rents> rentals;

    @JdbcType(VarbinaryJdbcType.class)
    private byte[] imageData;
}
