package com.crististinca.CarRentalAPI.controllers;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.crististinca.CarRentalAPI.model.Person;
import com.crististinca.CarRentalAPI.model.PersonDetailsService;
import com.crististinca.CarRentalAPI.security.JwtDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final PersonDetailsService personDetailsService;
    private final JwtDecoder jwtDecoder;

    @GetMapping("/by/username")
    public ResponseEntity<Person> findByUsername(@RequestParam("username") String username,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        Optional<DecodedJWT> maybeJWT = extractRolesFromToken(token);

        if (maybeJWT.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // User details can be extracted only by the users themselves or by admins
        DecodedJWT jwt = maybeJWT.get();
        List<String> roles = jwt.getClaim("au").asList(String.class);

        String claimedUsername = jwt.getClaim("e").asString();

        if (!(roles.contains("ADMIN") || (roles.contains("USER") && Objects.equals(claimedUsername, username)))) {
            return ResponseEntity.status(401).build();
        }

        Optional<Person> maybeUser = Optional.ofNullable(personDetailsService.getPersonByUsername(username));

        if (maybeUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(maybeUser.get());
    }

    @PostMapping
    public ResponseEntity<Person> addUser(@RequestBody Person person) {
        Optional<Person> maybeUser = Optional.ofNullable(personDetailsService.addPerson(person));

        if (maybeUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(maybeUser.get());
    }

    private Optional<DecodedJWT> extractRolesFromToken(String rawToken) {

        if (!(StringUtils.hasText(rawToken) && rawToken.startsWith("Bearer "))) {
            return Optional.empty();
        }

        String token = rawToken.substring(7);

        DecodedJWT jwt;
        try {
            jwt = jwtDecoder.decode(token);
        } catch (SignatureVerificationException | TokenExpiredException e) {
            return Optional.empty();
        }

        return Optional.of(jwt);
    }
}
