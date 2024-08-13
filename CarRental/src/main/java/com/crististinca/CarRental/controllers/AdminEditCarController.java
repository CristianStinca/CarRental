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
    public String saveCar(@ModelAttribute("car") Car car,
                          @RequestParam("image") MultipartFile file) throws IOException {
        Car origCar = carService.getCarById(carId);

        if (car.getBrand() != null)
            origCar.setBrand(car.getBrand());

        if (car.getModel() != null)
            origCar.setModel(car.getModel());

        if (file.isEmpty()) {
            origCar.setImageData(null);
        } else {
            origCar.setImageData(file.getBytes());
        }

        carService.update(origCar);
        return "redirect:/admin/cars";
    }
}
