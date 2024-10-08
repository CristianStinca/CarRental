package com.crististinca.CarRental.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Person {
    private Long id;

    @NotEmpty//(message = "User\'s name cannot be empty.")
    @Size(min = 2, max = 250)
    private String username;

    @NotEmpty//(message = "User\'s password cannot be empty.")
    @Size(min = 6, max = 250)
    private String password;

    private String role;

    private Client client;
}
