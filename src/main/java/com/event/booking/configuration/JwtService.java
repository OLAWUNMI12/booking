package com.event.booking.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final String secretKey;
    private final long jwtExpiry;

    public JwtService(@Value("${jwt.secret.key}") String secretKey, @Value("${jwt.expiry}") long jwtExpiry) {
        this.secretKey = secretKey;
        this.jwtExpiry = jwtExpiry;
    }

    public String retrieveUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date retrieveExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimFunction) {
        Claims claims = retrieveClaims(token);
        return claimFunction.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder().setClaims(claims).
                setSubject(userDetails.getUsername()).
                setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiry)).
                signWith(getSignInKey(), SignatureAlgorithm.HS256).
                compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = retrieveUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return retrieveExpiration(token).before(new Date());
    }


    private Claims retrieveClaims(String token) {
        return Jwts.parserBuilder().
                setSigningKey(getSignInKey()).
                build().parseClaimsJws(token).
                getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
