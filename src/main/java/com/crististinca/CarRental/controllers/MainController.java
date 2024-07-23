package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.model.Car;
import com.crististinca.CarRental.model.CarService;
import com.crististinca.CarRental.model.RentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/public")
public class MainController {

    @Autowired
    private CarService carService;

    @Autowired
    private RentsService rentsService;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        model.addAttribute("dateStr", "");
    }

    @GetMapping
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/set_date")
    public String handleMain(@RequestParam(value = "startDate") String startDateStr,
                             @RequestParam(value = "endDate") String endDateStr,
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

    @PostMapping
    public String selectCar(@RequestParam(value = "startDate", required = false) String startDateStr,
                            @RequestParam(value = "endDate", required = false) String endDateStr,
                            @RequestParam(value = "carId") String carId,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (startDateStr.isBlank() || endDateStr.isBlank()) {
            model.addAttribute("dateErr", "Pick a date first");
            return "index";
        }

        redirectAttributes.addAttribute("startDate", startDateStr);
        redirectAttributes.addAttribute("endDate", endDateStr);
        return "redirect:/public/cars/" + carId;
    }

    @PostMapping("/addcar")
    public String addCar(@RequestBody Car car) {
        carService.addCar(car);
        return "index";
    }
}
