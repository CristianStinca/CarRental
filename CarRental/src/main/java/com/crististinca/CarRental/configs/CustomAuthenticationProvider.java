package com.crististinca.CarRental.configs;

import com.crististinca.CarRental.Utils.WClient;
import com.crististinca.CarRental.model.LoginRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.client.RestClient;

import java.util.List;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    public CustomAuthenticationProvider(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(WClient.url).build();
    }

    private final RestClient restClient;

    @Override
    public Authentication authenticate (Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        ResponseEntity<LoginRequest> responseObj = this.restClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new LoginRequest(username, password))
                .retrieve()
                .toEntity(LoginRequest.class);

        List<String> list = responseObj.getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (list == null) {
            throw new BadCredentialsException("Bad credentials");
        }

        WClient.auth_token = list.getFirst();

        return super.authenticate(authentication);
    }
}