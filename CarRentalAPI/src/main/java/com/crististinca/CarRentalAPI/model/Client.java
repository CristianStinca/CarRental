package com.crististinca.CarRentalAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Rents> rentals;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    public void setNewPerson(Person person) {
        this.person = person;
    }
}
