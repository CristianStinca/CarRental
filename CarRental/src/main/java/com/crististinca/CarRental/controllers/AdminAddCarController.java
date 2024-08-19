package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.Utils.WClient;
import com.crististinca.CarRental.model.Car;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin/cars/add")
public class AdminAddCarController {

    public AdminAddCarController(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(WClient.url).build();
    }

    private final RestClient restClient;

    @ModelAttribute
    public void addCarForm(Model model) {
        model.addAttribute("car", new Car());
    }

    @GetMapping
    public String handleAddCar() {
        return "admin/caradd";
    }

    @PostMapping
    public String addCar(@ModelAttribute Car car,
                         @RequestParam("image") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            car.setImageData(null);
        } else {
            car.setImageData(file.getBytes());
        }

        Car responseCar = this.restClient.post()
                .uri("/car/details")
                .contentType(MediaType.APPLICATION_JSON)
                .body(car)
                .retrieve()
                .body(Car.class);

        return "redirect:/admin/cars";
    }
}
