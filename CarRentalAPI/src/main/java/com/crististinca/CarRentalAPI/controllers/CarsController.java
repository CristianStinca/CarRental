package com.crististinca.CarRentalAPI.controllers;

import com.crististinca.CarRentalAPI.model.Car;
import com.crististinca.CarRentalAPI.model.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cars")
public class CarsController {

    private final CarService carService;

    @Autowired
    public CarsController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/by/date")
    public ResponseEntity<List<Car>> getByDate(@RequestParam("startDate") LocalDate startDate,
                                               @RequestParam("endDate") LocalDate endDate) {
        List<Car> cars = carService.getAvailableCars(startDate, endDate).stream().filter(Car::getIsActive).toList();

        return ResponseEntity.ok(cars);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = carService.getAllCars().stream().filter(Car::getIsActive).toList();

        return ResponseEntity.ok(cars);
    }

    @GetMapping("/all/admin")
    public ResponseEntity<List<Car>> getAllCarsAdmin() {
        List<Car> cars = carService.getAllCars();

        return ResponseEntity.ok(cars);
    }
}
