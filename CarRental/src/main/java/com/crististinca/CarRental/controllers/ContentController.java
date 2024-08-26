package com.crististinca.CarRental.controllers;

import com.crististinca.CarRental.Utils.WClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
public class ContentController {

    @GetMapping("/login")
    public String handleLogin() {
        return "custom_login";
    }

    @GetMapping("/")
    public String handleMain() {
        return "redirect:/public";
    }

    @PostMapping("/jwt")
//    @ResponseBody
    public void handleJWT(@RequestParam String username,
                          @RequestParam String password) {
//        return "JWT: " + WClient.auth_token;
//        String attr = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
//        return attr;

        System.out.println(username);
        System.out.println(password);
    }
}