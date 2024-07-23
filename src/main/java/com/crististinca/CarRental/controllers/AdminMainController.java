package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.model.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminMainController {

    @Autowired
    public CarService carService;

    @ModelAttribute
    public void setModelAttributes(Model model) {
        model.addAttribute("cars", carService.getAllCars());
    }

    @GetMapping
    public String index() {
        return "admin/admin_index";
    }
}
