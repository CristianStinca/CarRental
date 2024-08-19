package com.crististinca.CarRentalAPI.controllers;

import com.crististinca.CarRentalAPI.model.ClientService;
import com.crististinca.CarRentalAPI.model.Person;
import com.crististinca.CarRentalAPI.model.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public UserController(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @GetMapping("/by/username")
    public ResponseEntity<Person> findByUsername(@RequestParam("username") String username) {
        Optional<Person> maybeUser = Optional.ofNullable(personDetailsService.getPersonByUsername(username));

        if (maybeUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(maybeUser.get());
    }

    @PostMapping
    public ResponseEntity<Person> addUser(@RequestBody Person person) {
        Optional<Person> maybeUser = Optional.ofNullable(personDetailsService.addPerson(person));

        if (maybeUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(maybeUser.get());
    }
}
