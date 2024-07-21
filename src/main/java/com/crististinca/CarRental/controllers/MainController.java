package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.model.Car;
import com.crististinca.CarRental.model.Person;
import com.crististinca.CarRental.repo.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private CarRepository carRepository;

    @GetMapping("/")
    public String handleMain() {
        return "index";
    }

    @PostMapping("/addCar")
    public String addCar(@ModelAttribute Car car) {
        carRepository.save(car);
        return "index";
    }
}
