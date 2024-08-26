package com.crististinca.CarRental.configs;

import com.crististinca.CarRental.Utils.RestClientCall;
import com.crististinca.CarRental.Utils.WClient;
import com.crististinca.CarRental.model.Person;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Component
public class BearerTokenAuthFilter extends OncePerRequestFilter {

    public BearerTokenAuthFilter(RestClient.Builder restClient) {
        this.restClientCall = new RestClientCall(restClient);
    }

    private final RestClientCall restClientCall;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

//        System.out.println("____________________");
////            System.out.println(email);
////        System.out.println(request.getHeaderNames().nextElement());
////        System.out.println(request.getHeader(HttpHeaders.HOST));
//        List<String> list = new ArrayList<>();
////        request.getHeaderNames().asIterator().forEachRemaining(list::add);
////        System.out.println(Arrays.stream(response.getHeaderNames().toArray()).toList());
//        System.out.println("____________________");

        if(authHeader != null && authHeader.startsWith("Bearer ") && !authHeader.substring(7).isBlank()) {
            String accessToken = authHeader.substring(7);
            WClient.auth_token = accessToken;

            String[] chunks = accessToken.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();

//            String header = new String(decoder.decode(chunks[0]));
            String payload = new String(decoder.decode(chunks[1]));
            JsonObject jsonObject= JsonParser.parseString(payload).getAsJsonObject();
            String email = jsonObject.get("e").getAsString();
            String roles = jsonObject.get("au").getAsString();

//            System.out.println("____________________");
//            System.out.println(email);
//            System.out.println(roles);
//            System.out.println("____________________");

            Person user = restClientCall.get(Person.class, "/users/by/username?userasdsname={username}", email);

            if(user == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            else {
                //todo: change the auth
                Authentication authenticationToken = new UsernamePasswordAuthenticationToken(email, null, Arrays.stream(user.getRole().split(",")).map(SimpleGrantedAuthority::new).toList());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}