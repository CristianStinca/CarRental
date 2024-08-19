package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.Utils.WClient;
import com.crististinca.CarRental.model.Person;
import com.crististinca.CarRental.repo.PersonRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Controller
@RequestMapping("/admin/register")
public class AdminRegisterController {

    public AdminRegisterController(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(WClient.url).build();
    }

    private final RestClient restClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("user", new Person());
    }

    @PostMapping
    public String createAdmin(@Valid @ModelAttribute("user") Person user,
                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "admin/custom_register_admin";
        }

        ResponseEntity<Person> responsePerson = this.restClient.get()
                .uri("/users/by/username?username={username}", user.getUsername())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null))
                .toEntity(Person.class);

        if (responsePerson.getStatusCode().is2xxSuccessful()) {
            bindingResult.rejectValue("username", "error.username", "This username already exists");
            return "admin/custom_register_admin";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ADMIN,USER");

        this.restClient.post().uri("/users").contentType(MediaType.APPLICATION_JSON).body(user).retrieve().toBodilessEntity();

        return "redirect:/admin";
    }

    @GetMapping
    public String handleAdminRequest() {
        return "admin/custom_register_admin";
    }
}