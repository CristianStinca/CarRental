package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.model.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/public/cars")
public class CarController {

    @Autowired
    private RentsService rentsService;

    @Autowired
    private CarService carService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private PersonDetailsService personDetailsService;

    @ModelAttribute
    void supplyModel(@PathVariable Long id,
                     @RequestParam("startDate") String startDateStr,
                     @RequestParam("endDate") String endDateStr,
                     Model model) {
        String authUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Person person = personDetailsService.getPersonByUsername(authUser);
        if (person != null && person.getClient() != null) {
            model.addAttribute("client", person.getClient());
        } else {
            model.addAttribute("client", new Client());
        }

        model.addAttribute("car", carService.getCarById(id));
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

        return "car_details";
    }

    @PostMapping("/{id}")
    public String saveForm(@PathVariable("id") Long carId,
                           @RequestParam("startDate") String startDateStr,
                           @RequestParam("endDate") String endDateStr,
                           @Valid @ModelAttribute Client client,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "car_details";
        };

        DateTimeFormatter formatterIn = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startDate = LocalDate.parse(startDateStr, formatterIn);
        LocalDate endDate = LocalDate.parse(endDateStr, formatterIn);

        Car car = carService.getCarById(carId);
        if (car == null) {
//            do something
            return "index";
        }

        if (!carService.isCarAvailable(car, startDate, endDate)) {
//            do something
            return "index";
        }

        if (clientService.findClientByEmail(client.getEmail()) != null) {
            return "index";
        }

        String authUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Person person = personDetailsService.getPersonByUsername(authUser);
        if (person != null) {
            if (person.getClient() == null) {
                client.setNewPerson(person);
            }
        }
        clientService.addClient(client);

        rentsService.rentCar(carId, startDate, endDate, clientService.findClientByEmail(client.getEmail()));

        return "index";
    }

}
