package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.Utils.WClient;
import com.crististinca.CarRental.model.Car;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin/cars/edit")
public class AdminEditCarController {

    public AdminEditCarController(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(WClient.url).build();
    }

    private final RestClient restClient;

    private Car car;

    @GetMapping("/{id}")
    public String editCar(@PathVariable Long id, Model model) {
        Car car;

        try {
            ResponseEntity<Car> responseCar = this.restClient.get()
                    .uri("/car/details?id={id}", id)
                    .retrieve()
                    .toEntity(Car.class);
            car = responseCar.getBody();
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
//    @ResponseBody
    public String saveCar(@ModelAttribute("car") Car car,
                          @RequestParam("image") MultipartFile file) throws IOException {
//        Car origCar = carService.getCarById(carId);
        Car origCar = this.car;

        if (car.getBrand() != null)
            origCar.setBrand(car.getBrand());

        if (car.getModel() != null)
            origCar.setModel(car.getModel());

        if (!file.isEmpty()) {
            origCar.setImageData(file.getBytes());
        }

//        carService.update(origCar);

        try {
            ResponseEntity<Car> updatedCar = restClient.put()
                    .uri("/car/details")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(origCar)
                    .retrieve()
                    .toEntity(Car.class);
        } catch (HttpClientErrorException.Unauthorized e) {
            //TODO: Show unauthorized.
            return "redirect:/admin?error=User unauthorized.";
        } catch (HttpClientErrorException.NotFound e) {
            //TODO: Show error that car was not found.
            return "redirect:/admin?error=Car not found.";
        } catch (HttpClientErrorException e) {
            //TODO: Show unexpected error happened.
            return "redirect:/admin?error=Unexpected error. Error message: " + e.getMessage();
        }

        return "redirect:/admin/cars?success=true";
    }
}
