package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.model.Car;
import com.crististinca.CarRental.model.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/cars/edit")
public class AdminEditCarController {

    @Autowired
    private CarService carService;

    private Long carId;

    @GetMapping("/{id}")
    public String editCar(@PathVariable Long id, Model model) {
        carId = id;
        Car car = carService.getCarById(id);
        model.addAttribute("car", car);
        return "admin/caredit";
    }

    @PostMapping("/save")
//    @ResponseBody
    public String saveCar(@ModelAttribute("car") Car car) {
        Car origCar = carService.getCarById(carId);
        origCar.setBrand(car.getBrand());
        origCar.setModel(car.getModel());
//        return origCar.toString();
        carService.update(origCar);
        return "redirect:/admin/cars";
    }
//    public String saveCar(@ModelAttribute("car") Car car) {
//        carService.update(car);
//        return "redirect:/admin/cars";
//    }
}
