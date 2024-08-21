package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.Utils.RestClientCall;
import com.crististinca.CarRental.Utils.WClient;
import com.crististinca.CarRental.model.Client;
import com.crististinca.CarRental.model.Person;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    public RegistrationController(RestClient.Builder restClientBuilder,
                                  PasswordEncoder passwordEncoder) {
        this.restClientCall = new RestClientCall(restClientBuilder);
        this.passwordEncoder = passwordEncoder;
    }

    private final RestClientCall restClientCall;

    private final PasswordEncoder passwordEncoder;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("user", new Person());
    }

    @PostMapping
    public String createUser(@Valid @ModelAttribute("user") Person user,
                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "custom_register";
        }

        boolean isPresentPerson = true;

        try {
            Person responsePerson = restClientCall.get(Person.class, "/users/by/username?username={username}", user.getUsername());
        } catch (HttpClientErrorException e) {
            isPresentPerson = false;
        }

        if (isPresentPerson) {
            bindingResult.rejectValue("username", "error.username", "This username already exists");
            return "custom_register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole("USER");
        }

        try {
            Person responsePerson = restClientCall.post(Person.class, "/users", user);
        } catch (HttpClientErrorException e) {
            //TODO: Show unexpected error happened.
            return "redirect:/public?error=unexpected_error";
        }

        return "redirect:/";
    }

    @GetMapping
    public String handleRequest() {
        return "custom_register";
    }

}