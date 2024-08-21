package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.Utils.BasicCarComparator;
import com.crististinca.CarRental.Utils.ImageUtil;
import com.crististinca.CarRental.Utils.RestClientCall;
import com.crististinca.CarRental.Utils.WClient;
import com.crististinca.CarRental.model.Car;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/public")
public class MainController {

    public MainController(RestClient.Builder restClientBuilder) {
        this.restClientCall = new RestClientCall(restClientBuilder);
    }

    private final RestClientCall restClientCall;

    private LocalDate _startDate;
    private LocalDate _endDate;

    private final DateTimeFormatter formatterOut = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter formatterIn = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private List<Car> cars = new ArrayList<>();

    @ModelAttribute
    public void addAttributes(Model model) throws IOException {

        try {
            cars = restClientCall.getList(Car.class, "/cars/all")
                    .stream().sorted(new BasicCarComparator()).toList();

            model.addAttribute("cars", cars);

        } catch (HttpClientErrorException.NotFound e) {
            //TODO: Show error that car was not found.
//            return "redirect:/public?error=car_not_found";
        } catch (HttpClientErrorException e) {
            //TODO: Show unexpected error happened.
//            return "redirect:/public?error=unexpected_error";
        }

        model.addAttribute("dateStr", "Pick a date");
        model.addAttribute("imgUtil", new ImageUtil("static/img/blank.jpg"));
    }

    @GetMapping
    public String handleMain(@RequestParam(value = "startDate", required = false) String startDateStr,
                             @RequestParam(value = "endDate", required = false) String endDateStr,
                             Model model) {

        if (startDateStr == null || endDateStr == null) {
            model.addAttribute("cars", cars);
            model.addAttribute("dateStr", "Pick a date");
            return "index";
        }

        _startDate = LocalDate.parse(startDateStr, formatterIn);
        _endDate = LocalDate.parse(endDateStr, formatterIn);

        String dateString = _startDate.format(formatterOut) + " - " + _endDate.format(formatterOut);

        model.addAttribute("dateStr", dateString);
        model.addAttribute("startDate", _startDate);
        model.addAttribute("endDate", _endDate);

        try {
            List<Car> carsByDate = restClientCall.getList(Car.class,
                            "/cars/by/date?startDate={_startDate}&endDate={_endDate}", _startDate, _endDate)
                    .stream().sorted(new BasicCarComparator()).toList();

            model.addAttribute("cars", carsByDate);

        } catch (HttpClientErrorException.NotFound e) {
            //TODO: Show error that car was not found.
            return "redirect:/public?error=car_not_found";
        } catch (HttpClientErrorException e) {
            //TODO: Show unexpected error happened.
            return "redirect:/public?error=unexpected_error";
        }

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
