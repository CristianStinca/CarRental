package com.crististinca.CarRental.configs;

import com.crististinca.CarRental.Utils.RestClientCall;
import com.crististinca.CarRental.Utils.WClient;
import com.crististinca.CarRental.model.LoginRequest;
import com.crististinca.CarRental.model.LoginResponse;
import com.crististinca.CarRental.model.Person;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

//    private final UserDetailsService userDetailsService;
//
    public CustomAuthenticationProvider(RestClient.Builder restClientBuilder) {
        this.restClientCall = new RestClientCall(restClientBuilder);
        this.restClient = restClientBuilder.build();
    }

    private final RestClientCall restClientCall;
    private final RestClient restClient;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
//        System.out.println(username);

//        restClientCall.post(LoginRequest.class, "/api/v1/auth/login", new LoginRequest(username, password));

        ResponseEntity<LoginRequest> responseObj = this.restClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new LoginRequest(username, password))
                .retrieve()
                .toEntity(LoginRequest.class);

        String token = responseObj.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

        WClient.auth_token = token;

        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        JsonObject jsonObject= JsonParser.parseString(payload).getAsJsonObject();
        String email = jsonObject.get("e").getAsString();
        String roles = jsonObject.get("au").getAsString();

//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//        if (!password.equals(userDetails.getPassword())) {
//            throw new BadCredentialsException("Invalid username or password");
//        }

        Authentication authenticatedToken = new UsernamePasswordAuthenticationToken(username, password,
                Arrays.stream(roles.split(",")).map(SimpleGrantedAuthority::new).toList());

        // Store the access and refresh token in the session
        SecurityContextHolder.getContext().setAuthentication(authenticatedToken);
        ServletRequestAttributes request = (ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes();
        HttpSession session = request.getRequest().getSession(false);
        session.setAttribute("accessToken", token);
//        session.setAttribute("refreshToken", responseBody.getRefreshToken());

        // Set session expiration based on JWT expiration time
//        final var jwtExpiration = claims.getExpiration();
//        final var sessionTimeoutInMillis = jwtExpiration.getTime() - System.currentTimeMillis();
//        final var sessionExpiration = sessionTimeoutInMillis / 1000;
//        session.setMaxInactiveInterval((int) sessionExpiration);

        return authenticatedToken;

//        return new UsernamePasswordAuthenticationToken(username, password,
//                Arrays.stream(roles.split(",")).map(SimpleGrantedAuthority::new).toList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}