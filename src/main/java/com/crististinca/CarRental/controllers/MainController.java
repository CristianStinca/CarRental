package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.model.Car;
import com.crististinca.CarRental.model.CarService;
import com.crististinca.CarRental.model.RentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private CarService carService;

    @Autowired
    private RentsService rentsService;

    @GetMapping
    public String handleMain(@RequestParam(value = "startDate", required = false) String startDateStr,
                             @RequestParam(value = "endDate", required = false) String endDateStr,
                             Model model) {

        if (startDateStr == null || endDateStr == null) {
            model.addAttribute("cars", carService.getAllCars());
            return "index";
        }

        //TODO: Delegate this to RestController
        DateTimeFormatter formatterOut = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter formatterIn = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startDate = LocalDate.parse(startDateStr, formatterIn);
        LocalDate endDate = LocalDate.parse(endDateStr, formatterIn);

        String dateString = startDate.format(formatterOut) + " - " + endDate.format(formatterOut);

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("dateStr", dateString);
        model.addAttribute("cars", carService.getAvailableCars(startDate, endDate));

        return "index";
    }

    @PostMapping("/addcar")
    public String addCar(@RequestBody Car car) {
        carService.addCar(car);
        return "index";
    }

//    @PostMapping("/addreserv")
//    public String addCar(@RequestParam("rental_date_start") String startDateStr,
//                         @RequestParam("rental_date_end") String endDateStr,
//                         @RequestParam("car_id") Long carId) {
//        DateTimeFormatter formatterIn = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate startDate = LocalDate.parse(startDateStr, formatterIn);
//        LocalDate endDate = LocalDate.parse(endDateStr, formatterIn);
//        rentsService.rentCar(carId, startDate, endDate);
//        return "index";
//    }
}
