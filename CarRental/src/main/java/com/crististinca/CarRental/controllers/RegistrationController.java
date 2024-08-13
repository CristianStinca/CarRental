package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.model.Person;
import com.crististinca.CarRental.repo.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class RegistrationController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String createUser(@ModelAttribute Person user, Model model) {
        model.addAttribute("user", user);
        Optional<Person> userOptional = personRepository.findByUsername(user.getUsername());
        if (userOptional.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole("USER");
        }
        personRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/register")
    public String handleRequest(Model model) {
        model.addAttribute("user", new Person());
        return "custom_register";
    }

    @PostMapping("/admin/register")
    public String createAdmin(@ModelAttribute Person user, Model model) {
        model.addAttribute("user", user);
        Optional<Person> userOptional = personRepository.findByUsername(user.getUsername());
        if (userOptional.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ADMIN,USER");
        personRepository.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/register")
    public String handleAdminRequest(Model model) {
        model.addAttribute("user", new Person());
        return "admin/custom_register_admin";
    }
}