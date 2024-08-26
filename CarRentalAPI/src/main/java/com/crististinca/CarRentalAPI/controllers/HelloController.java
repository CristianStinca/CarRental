package com.crististinca.CarRentalAPI.controllers;

import com.crististinca.CarRentalAPI.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HelloController {
    @GetMapping("/")
    public String hello() {
        return "Hello, world! This is public to everyone";
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Everyone can see this";
    }

    @GetMapping("/secured")
    public String secured(@AuthenticationPrincipal UserPrincipal principal) {
        return "This can only be seen by a logged in user. Your Email is: ";
//                + principal.getEmail() + " your ID: " + principal.getUserId();
    }

    @GetMapping("/admin")
    public String admin(@AuthenticationPrincipal UserPrincipal principal) {
        return "If you see this, you are an admin. Your ID: " + principal.getUserId() + ", and your role is: " + principal.getAuthorities().toArray()[0];
    }
}