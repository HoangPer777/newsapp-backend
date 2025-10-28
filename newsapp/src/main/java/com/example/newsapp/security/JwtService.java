package com.example.newsapp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
  private final Key key;
  private final long accessMillis;
  public JwtService(
    @Value("${app.jwt.secret:change-me-please-change-me-please-change-me}") String secret,
    @Value("${app.jwt.accessTtlMillis:900000}") long accessMillis) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
    this.accessMillis = accessMillis;
  }

  public String generateAccessToken(String email, Map<String,Object> claims) {
    long now = System.currentTimeMillis();
    return Jwts.builder()
      .setClaims(claims)
      .setSubject(email)
      .setIssuedAt(new Date(now))
      .setExpiration(new Date(now + accessMillis))
      .signWith(key, SignatureAlgorithm.HS256)
      .compact();
  }

  public String extractEmail(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build()
      .parseClaimsJws(token).getBody().getSubject();
  }

  public boolean isTokenValid(String token, UserDetails ud) {
    try {
      var claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
      return ud.getUsername().equals(claims.getSubject()) && claims.getExpiration().after(new Date());
    } catch (JwtException e) { return false; }
  }
}
