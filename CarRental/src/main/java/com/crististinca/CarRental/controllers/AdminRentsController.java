package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.Utils.WClient;
import com.crististinca.CarRental.model.Car;
import com.crististinca.CarRental.model.Client;
import com.crististinca.CarRental.model.Rents;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/rents")
public class AdminRentsController {

    public AdminRentsController(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(WClient.url).build();
    }

    private final RestClient restClient;

    @ModelAttribute
    public void addAttributes(Model model) {
        List<List<String>> rents = new ArrayList<>();
        List<Rents> allRents = this.restClient.get()
                .uri("/rents")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (allRents == null) {
            return;
        }

        for (Rents d : allRents) {
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
