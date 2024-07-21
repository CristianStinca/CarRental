package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.model.Car;
import com.crististinca.CarRental.repo.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class DateRangeController {

    @Autowired
    CarRepository carRepo;

    @GetMapping("/submitDates")
    public String submitDates(@RequestParam("startDate") String startDateStr,
                              @RequestParam("endDate") String endDateStr,
                              Model model) {
        DateTimeFormatter formatterOut = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter formatterIn = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startDate = LocalDate.parse(startDateStr, formatterIn);
        LocalDate endDate = LocalDate.parse(endDateStr, formatterIn);

        String dateString = startDate.format(formatterOut) + " - " + endDate.format(formatterOut);

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("dateStr", dateString);
        model.addAttribute("cars", carRepo.findAll());

        return "index";
    }

    @PostMapping("/admin/addcar")
    public ResponseEntity<Car> addCar(@RequestBody Car car) {
        carRepo.save(car);
        return ResponseEntity.ok(car);
    }
}
