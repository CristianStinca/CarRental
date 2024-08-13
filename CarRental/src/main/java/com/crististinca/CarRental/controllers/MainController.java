package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.Utils.ImageUtil;
import com.crististinca.CarRental.model.Car;
import com.crististinca.CarRental.model.CarService;
import com.crististinca.CarRental.model.RentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/public")
public class MainController {

    @Autowired
    private CarService carService;

    private LocalDate _startDate;
    private LocalDate _endDate;

    private final DateTimeFormatter formatterOut = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter formatterIn = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @ModelAttribute
    public void addAttributes(Model model) throws IOException {
        model.addAttribute("cars", carService.getAllCars());
        model.addAttribute("dateStr", "Pick a date");
        model.addAttribute("imgUtil", new ImageUtil("static/img/blank.jpg"));
    }

    @GetMapping
    public String handleMain(@RequestParam(value = "startDate", required = false) String startDateStr,
                             @RequestParam(value = "endDate", required = false) String endDateStr,
                             Model model) {

        if (startDateStr == null || endDateStr == null) {
            model.addAttribute("cars", carService.getAllCars());
            model.addAttribute("dateStr", "Pick a date");
            return "index";
        }

        //TODO: Delegate this to RestController

        _startDate = LocalDate.parse(startDateStr, formatterIn);
        _endDate = LocalDate.parse(endDateStr, formatterIn);

        String dateString = _startDate.format(formatterOut) + " - " + _endDate.format(formatterOut);
        model.addAttribute("dateStr", dateString);
        model.addAttribute("startDate", _startDate);
        model.addAttribute("endDate", _endDate);
        model.addAttribute("cars", carService.getAvailableCars(_startDate, _endDate));

        return "index";
    }

    @GetMapping("/pickCar")
    public String selectCar(@RequestParam(value = "carId") String carId,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (_startDate == null || _endDate == null) {
            model.addAttribute("dateErr", "Pick a date first");
            return "index";
        }

        redirectAttributes.addAttribute("startDate", _startDate.toString());
        redirectAttributes.addAttribute("endDate", _endDate.toString());
        return "redirect:/public/cars/" + carId;
    }
}
