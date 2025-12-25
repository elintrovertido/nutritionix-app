package com.nutritionix.authentication_service.service;

import com.nutritionix.authentication_service.model.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${private-key}")
    private String privateKey;

    public String generateToken(AuthUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles());
        claims.put("userName", user.getUserName());
        claims.put("email", user.getEmail());
        return createToken(claims, user.getUserName());
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setSubject(userName)
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, privateKey)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(privateKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserName(String token) {
        Claims claims = extractClaims(token);
        return claims.get("userName", String.class);
    }

    public Date getExpiration(String token) {
        Claims claims = extractClaims(token);
        return claims.getExpiration();
    }

}
