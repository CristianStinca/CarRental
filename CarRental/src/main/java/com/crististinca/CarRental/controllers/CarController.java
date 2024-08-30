package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.Utils.RestClientCall;
import com.crististinca.CarRental.model.Car;
import com.crististinca.CarRental.model.Client;
import com.crististinca.CarRental.model.Person;
import com.crististinca.CarRental.model.Rents;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/public/cars")
public class CarController {

    public CarController(RestClient.Builder restClientBuilder) {
        this.restClientCall = new RestClientCall(restClientBuilder);
    }

    private final RestClientCall restClientCall;

    @ModelAttribute
    void supplyModel(@PathVariable Long id,
                     @RequestParam("startDate") String startDateStr,
                     @RequestParam("endDate") String endDateStr,
                     Model model) {
        String authUser = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            Person responsePerson = restClientCall.get(Person.class, "/users/by/username?username={username}", authUser);
            model.addAttribute("client", responsePerson.getClient());
        } catch (HttpClientErrorException e) {
            model.addAttribute("client", new Client());
        }

        model.addAttribute("startDate", startDateStr);
        model.addAttribute("endDate", endDateStr);
        model.addAttribute("path", "/public/cars/" + id.toString());
    }

    @GetMapping("/{id}")
    public String getCarById(@RequestParam("startDate") String startDateStr,
                             @RequestParam("endDate") String endDateStr,
                             @PathVariable Long id,
                             @ModelAttribute Client client,
                             Model model) {

        try {
            Car responseCar = restClientCall.get(Car.class, "/car/details?id={id}", id);
            model.addAttribute("car", responseCar);
        } catch (HttpClientErrorException.NotFound e) {
            //TODO: Show error that car was not found.
            return "redirect:/public?error=car_not_found&id=" + id;
        } catch (HttpClientErrorException e) {
            //TODO: Show unexpected error happened.
            return "redirect:/public?error=unexpected_error";
        }

        return "car_details";
    }

    @PostMapping("/{id}")
    public String saveForm(@PathVariable("id") Long carId,
                           @RequestParam("startDate") String startDateStr,
                           @RequestParam("endDate") String endDateStr,
                           @Valid @ModelAttribute Client client,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "car_details";
        };

        DateTimeFormatter formatterIn = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startDate = LocalDate.parse(startDateStr, formatterIn);
        LocalDate endDate = LocalDate.parse(endDateStr, formatterIn);

        Car responseCar;

        try {
            responseCar = restClientCall.get(Car.class, "/car/details?id={carId}", carId);
        } catch (HttpClientErrorException.NotFound e) {
            //TODO: Show error that car was not found.
            return "redirect:/public?error=car_not_found&id=" + carId;
        } catch (HttpClientErrorException e) {
            //TODO: Show unexpected error happened.
            return "redirect:/public?error=unexpected_error";
        }

        try {
            restClientCall.get(Boolean.class,
                    "/car/details/available?id={carId}&startDate={startDate}&endDate={endDate}",
                    carId, startDate, endDate);
        } catch (HttpClientErrorException e) {
            //TODO: Show unexpected error happened.
            return "redirect:/";
        }

        String authUser = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            Person responsePerson = restClientCall.get(Person.class,
                    "/users/by/username?username={username}", authUser);
            if (responsePerson.getClient() == null)
                client.setNewPerson(responsePerson);
        } catch (HttpClientErrorException ignored) { }

        Client clientSaved;
        try {
            clientSaved = restClientCall.post(Client.class ,"/clients", client);
        } catch (HttpClientErrorException e) {
            //TODO: Show unexpected error happened.
            return "redirect:/public?error=unexpected_error";
        }

        Rents rent = new Rents();
        rent.setCar(responseCar);
        rent.setRentalDateStart(startDate);
        rent.setRentalDateEnd(endDate);
        rent.setClient(clientSaved);

        try {
            Rents responseRent = restClientCall.post(Rents.class,"/rents", rent);
        } catch (HttpClientErrorException e) {
            //TODO: Show unexpected error happened.
            return "redirect:/public?error=unexpected_error";
        }

        return "redirect:/";
    }

}
