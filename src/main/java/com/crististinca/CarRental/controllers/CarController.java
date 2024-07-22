package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.model.Car;
import com.crististinca.CarRental.model.CarService;
import com.crististinca.CarRental.model.Client;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    public String getCarById(@RequestParam("startDate") String startDateStr,
                             @RequestParam("endDate") String endDateStr,
                             @PathVariable Long id,
                             HttpServletRequest request,
                             Model model) {
//        return carService.getCarById(id);
        model.addAttribute("car", carService.getCarById(id));
        model.addAttribute("startDate", startDateStr);
        model.addAttribute("endDate", endDateStr);
        model.addAttribute("client", new Client());

        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null && inputFlashMap.containsKey("errors") && inputFlashMap.get("errors") != null) {
            List<ObjectError> errs = (List<ObjectError>) inputFlashMap.get("errors");
            model.addAttribute("errors", errs.toString());
            model.addAttribute("nameErr", errs.get(1).getCode());
        }
        return "car_details";
    }

}
