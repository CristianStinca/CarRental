package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.model.Car;
import com.crististinca.CarRental.model.Client;
import com.crististinca.CarRental.model.Rents;
import com.crististinca.CarRental.model.RentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/rents")
public class AdminRentsController {

    @Autowired
    private RentsService rentsService;

    @ModelAttribute
    public void addAttributes(Model model) {
        List<List<String>> rents = new ArrayList<>();
        for (Rents d : rentsService.getAllRentals()) {
            Client c = d.getClient();
            Car car = d.getCar();
            rents.add(Arrays.asList(d.getId().toString(),c.getName(), c.getEmail(), c.getAddress(), c.getPhoneNumber(), car.getId().toString(), car.getBrand(), car.getModel(), d.getRentalDateStart().toString(), d.getRentalDateEnd().toString()));
        }
        model.addAttribute("rentsH", Arrays.asList("Id", "ClientName", "ClientEmail", "ClientAddress", "ClientNumber", "CarId", "CarBrand", "CarModel", "RentalDateStart", "RentalDateEnd"));
        model.addAttribute("rents", rents);
    }

    @GetMapping
    public String adminRents(Model model) {
        return "admin/rents";
    }
}
