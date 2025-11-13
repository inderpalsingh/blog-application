package com.blogapi.config.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtHelper {

    private static final long ACCESS_TOKEN_VALIDITY = 5 * 60 * 1000; // 10 mins
    private static final long REFRESH_TOKEN_VALIDITY = 60 * 60 * 1000; // 60 mins

    private static final String SECRET = "yIGl0IGFuZCB3YW50cyB0byBoYXZlIGl0LCBzaW1wbHkgYmVjYXVzZSBpdCBpcyBwYWlu";

    private SecretKey key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    // generate access token
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("token_type", "access_token");

        return buildToken(tokenInfo, userDetails.getUsername(), ACCESS_TOKEN_VALIDITY);
    }

    // generate refresh token
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("token_type", "refresh_token");
        return buildToken(tokenInfo, userDetails.getUsername(), REFRESH_TOKEN_VALIDITY);
    }


    // check access token
    public boolean isAccessToken(String token) {
        return getTokenType(token).equals("access_token");
    }

    // check refresh token
    public boolean isRefreshToken(String token) {
        return getTokenType(token).equals("refresh_token");
    }


    // check token type
    public String getTokenType(String token) {
        Object tokenType = getClaims(token).get("token_type");
        return tokenType != null ? tokenType.toString() : "";
    }


    private String buildToken(Map<String, Object> tokenInfo, String subject, long validity) {
        return Jwts.builder()
                .claims(tokenInfo)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + validity))
                .signWith(key)
                .compact();
    }

    // generate token from user
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    // get all claims from token
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // token validate
    public boolean isValidToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // check token expiration
    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

}
