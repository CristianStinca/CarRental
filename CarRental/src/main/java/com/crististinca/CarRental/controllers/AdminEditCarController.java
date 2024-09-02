package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.Utils.RestClientCall;
import com.crististinca.CarRental.model.Car;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin/cars/edit")
public class AdminEditCarController {

    public AdminEditCarController(RestClient.Builder restClientBuilder) {
        this.restClientCall = new RestClientCall(restClientBuilder);
    }

    private final RestClientCall restClientCall;

    private Car car;

    @GetMapping("/{id}")
    public String editCar(@PathVariable Long id, Model model) {

        Car car;
        try {
            car = restClientCall.get(Car.class, "/car/details?id={id}", id);
        } catch (HttpClientErrorException.NotFound e) {
            //TODO: Show error that car was not found.
            return "redirect:/admin?error=Car not found.";
        } catch (HttpClientErrorException e) {
            //TODO: Show unexpected error happened.
            return "redirect:/admin?error=Unexpected error. Error message: " + e.getMessage();
        }

        this.car = car;
        model.addAttribute("car", car);
        return "admin/caredit";
    }

    @PostMapping("/save")
    public String saveCar(@ModelAttribute("car") Car car,
                          BindingResult bindingResult,
                          @RequestParam(name = "image", required = false) MultipartFile file) throws IOException {

        if (bindingResult.hasErrors()) {
            return "admin/caredit";
        }

        Car origCar = this.car;

        if (car.getBrand() != null)
            origCar.setBrand(car.getBrand());

        if (car.getModel() != null)
            origCar.setModel(car.getModel());

        if (file != null && !file.isEmpty()) {
            origCar.setImageData(file.getBytes());
        }

        try {
            Car updatedCar = restClientCall.put(Car.class, "/car/details", origCar);
        } catch (HttpClientErrorException.Unauthorized e) {
            //TODO: Show unauthorized.
            return "redirect:/admin?error=user_unauthorized";
        } catch (HttpClientErrorException.NotFound e) {
            //TODO: Show error that car was not found.
            return "redirect:/admin?error=car_not_found";
        } catch (HttpClientErrorException e) {
            //TODO: Show unexpected error happened.
            return "redirect:/admin?error=unexpected_error";
        }

        return "redirect:/admin/cars?success=true";
    }

    @GetMapping("/deactivate/{id}")
    public String deactivateCar(@PathVariable Long id) {

        Car car;
        try {
            car = restClientCall.get(Car.class, "/car/details?id={id}", id);
        } catch (HttpClientErrorException.NotFound e) {
            //TODO: Show error that car was not found.
            return "redirect:/admin?error=Car not found.";
        } catch (HttpClientErrorException e) {
            //TODO: Show unexpected error happened.
            return "redirect:/admin?error=Unexpected error. Error message: " + e.getMessage();
        }

        car.setIsActive(false);

        try {
            restClientCall.put(Car.class, "/car/details", car);
        } catch (HttpClientErrorException.Unauthorized e) {
            //TODO: Show unauthorized.
            return "redirect:/admin?error=user_unauthorized";
        } catch (HttpClientErrorException.NotFound e) {
            //TODO: Show error that car was not found.
            return "redirect:/admin?error=car_not_found";
        } catch (HttpClientErrorException e) {
            //TODO: Show unexpected error happened.
            return "redirect:/admin?error=unexpected_error";
        }

        return "redirect:/admin/cars";
    }

    @GetMapping("/activate/{id}")
    public String activateCar(@PathVariable Long id) {

        Car car;
        try {
            car = restClientCall.get(Car.class, "/car/details?id={id}", id);
        } catch (HttpClientErrorException.NotFound e) {
            //TODO: Show error that car was not found.
            return "redirect:/admin?error=Car not found.";
        } catch (HttpClientErrorException e) {
            //TODO: Show unexpected error happened.
            return "redirect:/admin?error=Unexpected error. Error message: " + e.getMessage();
        }

        car.setIsActive(true);

        try {
            restClientCall.put(Car.class, "/car/details", car);
        } catch (HttpClientErrorException.Unauthorized e) {
            //TODO: Show unauthorized.
            return "redirect:/admin?error=user_unauthorized";
        } catch (HttpClientErrorException.NotFound e) {
            //TODO: Show error that car was not found.
            return "redirect:/admin?error=car_not_found";
        } catch (HttpClientErrorException e) {
            //TODO: Show unexpected error happened.
            return "redirect:/admin?error=unexpected_error";
        }

        return "redirect:/admin/cars";
    }
}
