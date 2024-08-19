package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.Utils.WClient;
import com.crististinca.CarRental.model.Car;
import com.crististinca.CarRental.model.Client;
import com.crististinca.CarRental.model.Person;
import com.crististinca.CarRental.model.Rents;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/public/cars")
public class CarController {

    public CarController(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(WClient.url).build();
    }

    private final RestClient restClient;

    @ModelAttribute
    void supplyModel(@PathVariable Long id,
                     @RequestParam("startDate") String startDateStr,
                     @RequestParam("endDate") String endDateStr,
                     Model model) {
        String authUser = SecurityContextHolder.getContext().getAuthentication().getName();

        ResponseEntity<Person> responsePerson = this.restClient.get()
                .uri("/users/by/username?username={username}", authUser)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null))
                .toEntity(Person.class);

        if (responsePerson.getStatusCode().is2xxSuccessful() && responsePerson.getBody() != null && responsePerson.getBody().getClient() != null) {
            model.addAttribute("client", responsePerson.getBody().getClient());
        } else {
            model.addAttribute("client", new Client());
        }

        model.addAttribute("car", this.restClient.get().uri("/car/details?id={id}", id).retrieve().body(Car.class));
        model.addAttribute("startDate", startDateStr);
        model.addAttribute("endDate", endDateStr);
        model.addAttribute("path", "/public/cars/" + id.toString());
    }

    @GetMapping("/{id}")
    public String getCarById(@RequestParam("startDate") String startDateStr,
                             @RequestParam("endDate") String endDateStr,
                             @PathVariable Long id,
                             @ModelAttribute Client client,
                             Model model) {

        return "car_details";
    }

    @PostMapping("/{id}")
    public String saveForm(@PathVariable("id") Long carId,
                           @RequestParam("startDate") String startDateStr,
                           @RequestParam("endDate") String endDateStr,
                           @Valid @ModelAttribute Client client,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "car_details";
        };

        DateTimeFormatter formatterIn = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startDate = LocalDate.parse(startDateStr, formatterIn);
        LocalDate endDate = LocalDate.parse(endDateStr, formatterIn);

        ResponseEntity<Car> responseCar = this.restClient.get()
                .uri("/car/details?id={carId}", carId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null))
                .toEntity(Car.class);

        if (responseCar.getStatusCode().is4xxClientError()) {
            return "redirect:/";
        }

        ResponseEntity<Boolean> responseAvailable = this.restClient.get()
                .uri("/car/details/available?id={carId}&startDate={startDate}&endDate={endDate}", carId, startDate, endDate)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null))
                .toEntity(Boolean.class);

        if (responseAvailable.getStatusCode().is4xxClientError() || Boolean.FALSE.equals(responseAvailable.getBody())) {
            return "redirect:/";
        }

//        ResponseEntity<Client> responseClient = this.restClient.get().uri("/clients/by/email?email={email}", client.getEmail())
//                .retrieve()
//                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null))
//                .toEntity(Client.class);
//
//        System.out.println("----------------- passed clients/by/email ---------------------");
//
//        if (responseClient.getStatusCode().is4xxClientError()) {
//            return "redirect:/";
//        }

        String authUser = SecurityContextHolder.getContext().getAuthentication().getName();

        ResponseEntity<Person> responsePerson = this.restClient.get()
                .uri("/users/by/username?username={username}", authUser)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null))
                .toEntity(Person.class);

        if (responsePerson.getStatusCode().is2xxSuccessful() && responsePerson.getBody() != null && responsePerson.getBody().getClient() != null) {
            if (responsePerson.getBody().getClient() == null) {
                client.setNewPerson(responsePerson.getBody());
            }
        }

        Client clientSaved = this.restClient.post().uri("/clients", client).contentType(MediaType.APPLICATION_JSON).body(client).retrieve().body(Client.class);

        Rents rent = new Rents();
        rent.setCar(responseCar.getBody());
        rent.setRentalDateStart(startDate);
        rent.setRentalDateEnd(endDate);
        rent.setClient(clientSaved);

        this.restClient.post().uri("/rents").contentType(MediaType.APPLICATION_JSON).body(rent).retrieve().toBodilessEntity();

        return "redirect:/";
    }

}
