package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.Utils.RestClientCall;
import com.crististinca.CarRental.Utils.WClient;
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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Controller
@RequestMapping("/admin/register")
public class AdminRegisterController {

    public AdminRegisterController(RestClient.Builder restClientBuilder,
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
    public String createAdmin(@Valid @ModelAttribute("user") Person user,
                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "admin/custom_register_admin";
        }

        boolean isPresentPerson = true;

        try {
            Person responsePerson = restClientCall.get(Person.class, "/users/by/username?username={username}", user.getUsername());
        } catch (HttpClientErrorException e) {
            isPresentPerson = false;
        }

        if (isPresentPerson) {
            bindingResult.rejectValue("username", "error.username", "This username already exists");
            return "admin/custom_register_admin";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ADMIN,USER");

        try {
            Person responsePerson = restClientCall.post(Person.class, "/users", user);
        } catch (HttpClientErrorException e) {
            //TODO: Show unexpected error happened.
            return "redirect:/admin?error=unexpected_error";
        }

        return "redirect:/admin";
    }

    @GetMapping
    public String handleAdminRequest() {
        return "admin/custom_register_admin";
    }
}