package com.icore.ecommerce_platform.serviceimpl;

import com.icore.ecommerce_platform.dao.TokenRepository;
import com.icore.ecommerce_platform.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Issues and validates JSON Web Tokens. Tokens are signed with an HMAC key derived
 * from the {@code app.jwt.secret} property; validity is additionally cross-checked
 * against the token store so that logged-out tokens are rejected.
 */
@Service
public class JwtServiceImpl {


    private final String secretKey;

    private final TokenRepository tokenRepository;

    private static final long ACCESS_TOKEN_EXPIRY_MS = 15 * 60 * 1000L;            // 15 minutes
    private static final long REFRESH_TOKEN_EXPIRY_MS = 7L * 24 * 60 * 60 * 1000;  // 7 days

    public JwtServiceImpl(@Value("${app.jwt.secret}") String secretKey, TokenRepository tokenRepository) {
        this.secretKey = secretKey;
        this.tokenRepository = tokenRepository;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);

        boolean isValidToken = tokenRepository.findByToken(token)
                .map(t -> !t.isLoggedOut()).orElse(false);
        return (username.equals(user.getUsername())) && !isTokenExpired(token) && isValidToken;

    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(User user) {
        return buildToken(user, ACCESS_TOKEN_EXPIRY_MS);
    }

    public String generateRefreshToken(User user) {
        return buildToken(user, REFRESH_TOKEN_EXPIRY_MS);
    }

    private String buildToken(User user, long expiryMs) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiryMs))
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
