package com.crististinca.CarRentalAPI.controllers;

import com.crististinca.CarRentalAPI.model.AuthService;
import com.crististinca.CarRentalAPI.model.LoginRequest;
import com.crististinca.CarRentalAPI.model.LoginResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

//    @PostMapping("/login")
//    public LoginResponse login(@RequestBody @Validated LoginRequest request) {
//        return authService.attemptLogin(request.getEmail(), request.getPassword());
//    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Validated LoginRequest request,
                                               HttpServletResponse response) {
        LoginResponse loginResponse = authService.attemptLogin(request.getEmail(), request.getPassword());
        Cookie cookie = new Cookie("auth", loginResponse.getToken());

        //add cookie to response
        response.addCookie(cookie);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, loginResponse.getToken())
                .body(loginResponse);
    }
}