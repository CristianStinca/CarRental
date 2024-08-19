package com.crististinca.CarRental.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/admin")
public class AdminMainController {

    @GetMapping
    public String index() {
        return "admin/admin_index";
    }

}
