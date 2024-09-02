package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.Utils.RestClientCall;
import com.crististinca.CarRental.model.Car;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/cars/add")
public class AdminAddCarController {

    public AdminAddCarController(RestClient.Builder restClientBuilder) {
        this.restClientCall = new RestClientCall(restClientBuilder);
    }

    private final RestClientCall restClientCall;

    @ModelAttribute
    public void addCarForm(Model model) {
        model.addAttribute("car", new Car());
    }

    @GetMapping
    public String handleAddCar() {
        return "admin/caradd";
    }

    @PostMapping
    public String addCar(@Valid @ModelAttribute("car") Car car,
                         BindingResult bindingResult,
                         @RequestParam("image") MultipartFile file) throws IOException {

        if (bindingResult.hasErrors()) {
            return "admin/caradd";
        };

        if (file.isEmpty()) {
            car.setImageData(null);
        } else {
            car.setImageData(file.getBytes());
        }

        try {
            Car responseCar = restClientCall.post(Car.class, "/car/details", car);
        } catch (HttpClientErrorException e) {
            //TODO: Show unexpected error happened.
            return "redirect:/admin?error=unexpected_error";
        }

        return "redirect:/admin/cars";
    }
}
