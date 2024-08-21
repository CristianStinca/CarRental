package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.Utils.RestClientCall;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/rents")
public class AdminRentsController {

    public AdminRentsController(RestClient.Builder restClientBuilder) {
        this.restClientCall = new RestClientCall(restClientBuilder);
    }

    private final RestClientCall restClientCall;

    @ModelAttribute
    public void addAttributes(Model model) {

        try {
            List<Rents> allRents = restClientCall.getList(Rents.class, "/rents");

            List<List<String>> rents = new ArrayList<>();
            for (Rents d : allRents) {
                Client c = d.getClient();
                Car car = d.getCar();
                rents.add(Arrays.asList(d.getId().toString(),c.getName(), c.getEmail(), c.getAddress(), c.getPhoneNumber(), car.getId().toString(), car.getBrand(), car.getModel(), d.getRentalDateStart().toString(), d.getRentalDateEnd().toString()));
            }

            model.addAttribute("rentsH", Arrays.asList("Id", "ClientName", "ClientEmail", "ClientAddress", "ClientNumber", "CarId", "CarBrand", "CarModel", "RentalDateStart", "RentalDateEnd"));
            model.addAttribute("rents", rents);
        } catch (HttpClientErrorException.NotFound e) {
            //TODO: Show error that rents were not found.
//            return "redirect:/public?error=car_not_found&id=" + id;
        } catch (HttpClientErrorException e) {
            //TODO: Show unexpected error happened.
//            return "redirect:/public?error=unexpected_error";
        }
    }

    @GetMapping
    public String adminRents() {
        return "admin/rents";
    }
}
