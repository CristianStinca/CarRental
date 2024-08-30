package com.crististinca.CarRental.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class Client {
    private Long id;

    @NotEmpty(message = "Name cannot be empty.")
    @Size(min = 2, max = 250)
    private String name;

    @NotEmpty(message = "E-mail cannot be empty.")
    @Email
    @Size(min = 2, max = 250)
    private String email;

    @NotEmpty(message = "Address cannot be empty.")
    @Size(min = 1, max = 250)
    private String address;

    @NotEmpty(message = "Phone number cannot be empty.")
    @Size(min = 2, max = 250)
    private String phoneNumber;

    private Set<Rents> rentals;

    private Person person;

    public void setNewPerson(Person person) {
        this.person = person;
    }
}
