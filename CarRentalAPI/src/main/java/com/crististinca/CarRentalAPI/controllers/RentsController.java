package com.crististinca.CarRentalAPI.controllers;

import com.crististinca.CarRentalAPI.model.Rents;
import com.crististinca.CarRentalAPI.model.RentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/rents")
public class RentsController {

    private final RentsService rentsService;

    @Autowired
    public RentsController(RentsService rentsService) {
        this.rentsService = rentsService;
    }

    @GetMapping
    public ResponseEntity<List<Rents>> getAllRents() {
        List<Rents> rents = this.rentsService.getAllRentals();
        return ResponseEntity.ok(rents);
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
