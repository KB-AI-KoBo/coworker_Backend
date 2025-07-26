package com.kobo.coworker.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class TokenProvider {

    private static final String USERNAME_CLAIM = "Username";

    private final Key key;
    private final long accessTokenValidityTime;
    private final long refreshTokenValidityTime;

    public TokenProvider(@Value("${jwt.secret}") String secretKey,
                         @Value("${jwt.access-token-validity-in-milliseconds}") long accessTokenValidityTime,
                         @Value("${jwt.refresh-token-validity-in-milliseconds}") long refreshTokenValidityTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidityTime = accessTokenValidityTime;
        this.refreshTokenValidityTime = refreshTokenValidityTime;
    }

    public String createAccessToken(String userId, String email) {
        long nowTime = (new Date()).getTime();
        Date expiration = new Date(nowTime + accessTokenValidityTime);

        return Jwts.builder()
                .setSubject(userId)
                .claim(USERNAME_CLAIM, email)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String userId, String email) {
        long nowTime = (new Date()).getTime();
        Date expiration = new Date(nowTime + refreshTokenValidityTime);

        return Jwts.builder()
                .setSubject(userId)
                .claim(USERNAME_CLAIM, email)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).get(USERNAME_CLAIM, String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

}
