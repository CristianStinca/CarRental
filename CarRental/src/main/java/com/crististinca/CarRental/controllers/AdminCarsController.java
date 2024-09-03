package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.Utils.BasicCarComparator;
import com.crististinca.CarRental.Utils.ImageUtil;
import com.crististinca.CarRental.Utils.RestClientCall;
import com.crististinca.CarRental.model.Car;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/cars")
public class AdminCarsController {

    public AdminCarsController(RestClient.Builder restClientBuilder) {
        this.restClientCall = new RestClientCall(restClientBuilder);
    }

    private final RestClientCall restClientCall;

    private List<Car> cars;

    @ModelAttribute
    public void setModelAttributes(Model model) throws IOException {

        try {
            cars = restClientCall.getList(Car.class, "/cars/all/admin")
                    .stream().sorted(new BasicCarComparator()).toList();

            model.addAttribute("cars", cars);

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

    @GetMapping("/{id}")
    public String goToEditCars(@PathVariable Long id) {
        return "redirect:/admin/cars/edit/" + id;
    }
}
