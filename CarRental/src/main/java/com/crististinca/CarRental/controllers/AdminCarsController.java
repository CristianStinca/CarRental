package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.Utils.ImageUtil;
import com.crististinca.CarRental.Utils.WClient;
import com.crististinca.CarRental.model.Car;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/cars")
public class AdminCarsController {

    public AdminCarsController(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(WClient.url).build();
    }

    private final RestClient restClient;

    @ModelAttribute
    public void setModelAttributes(Model model) throws IOException {

        try {
            List<Car> responseCars = this.restClient.get()
                    .uri("/cars/all")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            model.addAttribute("cars", responseCars);

        } catch (HttpClientErrorException.NotFound e) {
            //TODO: Show error that car was not found.
//            return "redirect:/admin?error=Car not found.";
        } catch (HttpClientErrorException e) {
            //TODO: Show unexpected error happened.
//            return "redirect:/admin?error=Unexpected error. Error message: " + e.getMessage();
        }

        model.addAttribute("imgUtil", new ImageUtil("static/img/blank.jpg"));
    }

    @GetMapping
    public String adminCars() {
        return "admin/cars";
    }
}
