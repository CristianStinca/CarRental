package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.model.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/rentals")
public class RentsController {

    @Autowired
    private RentsService rentsService;

    @Autowired
    private CarService carService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private PersonDetailsService personDetailsService;

//    @GetMapping
//    public String getAllRentals(Model model) {
//        model.addAttribute("client", new Client());
//        return "rentals";
//    }

    @PostMapping
    public String rentCar(@RequestParam("startDate") String startDateStr,
                          @RequestParam("endDate") String endDateStr,
                          @RequestParam("car") Long carId,
                          @Valid @ModelAttribute Client client,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("startDate", startDateStr);
            redirectAttributes.addAttribute("endDate", endDateStr);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/cars/" + carId.toString();
        }

        model.addAttribute("client", client);

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

        String authUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Person person = personDetailsService.getPersonByUsername(authUser);
        if (person != null) {
            if (person.getClient() == null) {
                client.setNewPerson(person);
            }
        }

        Client clientFind = clientService.findClientByEmail(client.getEmail());
        if (clientFind == null) {
            clientService.addClient(client);
        } else {
            //tell the user the mail is repeated
            return "index";
        }

        rentsService.rentCar(carId, startDate, endDate, client);

        return "index";
    }

}
