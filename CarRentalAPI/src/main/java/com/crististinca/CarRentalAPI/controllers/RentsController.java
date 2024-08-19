package com.crististinca.CarRentalAPI.controllers;

import com.crististinca.CarRentalAPI.model.Rents;
import com.crististinca.CarRentalAPI.model.RentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/rents")
public class RentsController {

    private final RentsService rentsService;

    @Autowired
    public RentsController(RentsService rentsService) {
        this.rentsService = rentsService;
    }

    @PostMapping
    public ResponseEntity<Rents> createRent(@RequestBody Rents rent) {
        Optional<Rents> maybeRent = Optional.ofNullable(this.rentsService.rentCar(rent));
        if (maybeRent.isPresent()) {
            return ResponseEntity.ok().body(maybeRent.get());
        }

        return ResponseEntity.notFound().build();
    }
}
