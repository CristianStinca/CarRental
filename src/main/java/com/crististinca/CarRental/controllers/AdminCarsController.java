package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.Utils.ImageUtil;
import com.crististinca.CarRental.model.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/admin/cars")
public class AdminCarsController {

    @Autowired
    public CarService carService;

    @ModelAttribute
    public void setModelAttributes(Model model) throws IOException {
        model.addAttribute("cars", carService.getAllCars());
        model.addAttribute("imgUtil", new ImageUtil("static/img/blank.jpg"));
    }

    @GetMapping
    public String adminCars() {
        return "admin/cars";
    }
}
