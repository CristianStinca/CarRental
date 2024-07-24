package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.model.Car;
import com.crististinca.CarRental.model.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/cars/add")
public class AdminAddCarController {

    @Autowired
    private CarService carService;

    @ModelAttribute
    public void addCarForm(Model model) {
        model.addAttribute("car", new Car());
    }

    @GetMapping
    public String handleAddCar(Model model) {
        return "admin/caradd";
    }

    @PostMapping
    public String addCar(@ModelAttribute Car car) {
        carService.addCar(car);

        return "redirect:/admin/cars";
    }
}
