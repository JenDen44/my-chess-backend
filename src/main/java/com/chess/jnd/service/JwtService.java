package com.chess.jnd.service;

import com.chess.jnd.entity.Color;
import com.chess.jnd.error_handling.TokenNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    private final ObjectMapper mapper;

    @Autowired
    public JwtService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String generateCustomToken(Integer gameId, Color color) throws JsonProcessingException {
        byte[] secretKeyBytes = secretKey.getBytes();
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
        String mappedColor = mapper.writeValueAsString(color);

        Instant now = Instant.now();
        String token = Jwts.builder()
                .subject(String.valueOf(gameId))
                .issuedAt(Date.from(now))
                .claim("Color", mappedColor)
                .signWith(secretKey)
                .compact();

        return token;
    }

    public Integer getGameId(String token) throws JsonProcessingException {
        return getClaim(token, Integer.class, "sub");
    }

    public Color getColor(String token) throws JsonProcessingException {
        return getClaim(token, Color.class, "Color");
    }

    private  <T> T getClaim(String token, Class<T> tClass, String claimName) throws JsonProcessingException {
        byte[] secretKeyBytes = secretKey.getBytes();
        SecretKey key = Keys.hmacShaKeyFor(secretKeyBytes);

        JwtParser parser = Jwts.parser()
                .verifyWith(key)
                .build();

        Claims claims = parser.parseSignedClaims(token).getPayload();
        T claim = mapper.readValue((String) claims.get(claimName), tClass);

        return claim;

    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        throw new TokenNotFoundException("Token is not specified in Authorization header");
    }
}
