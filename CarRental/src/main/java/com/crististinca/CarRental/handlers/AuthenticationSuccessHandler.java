package com.crististinca.CarRental.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

//        List<String> list = new ArrayList<>();
//        request.getHeaderNames().asIterator().forEachRemaining(list::add);
//        System.out.println(list);
//        System.out.println(response.getHeaderNames().stream().toList());
//        System.out.println(authentication.getPrincipal());

//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//             var arr = Arrays.stream(cookies)
//                    .map(c -> c.getName() + "=" + c.getValue()).collect(Collectors.joining(", "));
//             System.out.println(arr[0]);
//        }

//        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        // Access the raw password from the token
//        String rawPassword = token.getCredentials().toString();
//        System.out.println("Password provided: " + rawPassword);

        if (isAdmin) {
            setDefaultTargetUrl("/admin");
        } else {
            setDefaultTargetUrl("/");
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
