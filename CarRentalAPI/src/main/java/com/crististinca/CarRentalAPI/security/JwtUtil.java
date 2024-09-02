package com.crististinca.CarRentalAPI.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtDecoder jwtDecoder;


}
