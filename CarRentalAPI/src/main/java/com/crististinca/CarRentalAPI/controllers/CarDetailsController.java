package com.crististinca.CarRentalAPI.controllers;

import com.crististinca.CarRentalAPI.model.Car;
import com.crististinca.CarRentalAPI.model.CarService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/car/details")
public class CarDetailsController {

    private final CarService carService;

    @Autowired
    public CarDetailsController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<Car> getById(@RequestParam Long id) {
        Optional<Car> maybeCar = Optional.ofNullable(carService.getCarById(id));

        if (maybeCar.isPresent()) {
            return ResponseEntity.ok(maybeCar.get());
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/available")
    public ResponseEntity<Boolean> isCarAvailable(@RequestParam Long id,
                                       @RequestParam("startDate") LocalDate startDate,
                                       @RequestParam("endDate") LocalDate endDate) {
        Optional<Car> maybeCar = Optional.ofNullable(carService.getCarById(id));

        if (maybeCar.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Car car = maybeCar.get();

        if (!car.getIsActive()) {
            return ResponseEntity.notFound().build();
        }

        Boolean isCarAvailable = carService.isCarAvailable(car, startDate, endDate);

        return ResponseEntity.ok(isCarAvailable);
    }

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<Car> addCar(@RequestBody Car car) {
        car.setId(null);

        carService.addCar(car);

        return ResponseEntity.ok(car);
    }

    @PutMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<Car> updateCar(@RequestBody Car car) {
        Optional<Car> maybeCar = Optional.ofNullable(carService.getCarById(car.getId()));

        if (maybeCar.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Car toUpdateCar = maybeCar.get();
        toUpdateCar.setImageData(car.getImageData());
        toUpdateCar.setIsActive(car.getIsActive());
        toUpdateCar.setBrand(car.getBrand());
        toUpdateCar.setModel(car.getModel());
        toUpdateCar.setPrice(car.getPrice());

        Car carUpdated = carService.update(toUpdateCar);

        return ResponseEntity.ok(carUpdated);
    }
}
