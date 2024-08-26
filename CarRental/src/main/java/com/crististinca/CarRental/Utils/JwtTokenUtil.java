//package com.crististinca.CarRental.Utils;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class JwtTokenUtil {
//
//    public Claims getAllClaimsFromToken(String token) {
//        return Jwts.parser()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    public String getUsernameFromToken(String token) {
//        return getAllClaimsFromToken(token).getSubject();
//    }
//
//    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
//        Claims claims = getAllClaimsFromToken(token);
//        return Arrays.stream(claims.get("roles").toString().split(","))
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//    }
//
//    public UserDetails getUserDetailsFromToken(String token) {
//        Claims claims = getAllClaimsFromToken(token);
//        String username = claims.getSubject();
//        List<SimpleGrantedAuthority> roles = getRolesFromToken(token);
//        return new User(username, "", roles);
//    }
//
//    public Boolean validateToken(String token, UserDetails userDetails) {
//        final String username = getUsernameFromToken(token);
//        return (username.equals(userDetails.getUsername()));
//    }
//}
