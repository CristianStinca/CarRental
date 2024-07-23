package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.model.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/cars/edit")
public class AdminEditCarController {

    @Autowired
    private CarService carService;

    @GetMapping("/{id}")
    public String editCar(@PathVariable Long id, Model model) {
        model.addAttribute("car", carService.getCarById(id));
        return "admin/caredit";
    }
}
