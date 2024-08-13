package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.model.Car;
import com.crististinca.CarRental.model.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public String handleAddCar() {
        return "admin/caradd";
    }

    @PostMapping
    public String addCar(@ModelAttribute Car car,
                         @RequestParam("image") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            car.setImageData(null);
        } else {
            car.setImageData(file.getBytes());
        }
        carService.addCar(car);

        return "redirect:/admin/cars";
    }
}
