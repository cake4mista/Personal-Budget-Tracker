package com.example.budgettracker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTGenerator {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long JWT_EXPIRATION = SecurityConstants.JWT_EXPIRATION;

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            System.out.println("DEBUG: Starting token validation...");
            System.out.println("DEBUG: Raw Token: '" + token + "'");

            // Check for null or whitespace token
            if (token == null || token.trim().isEmpty()) {
                System.out.println("DEBUG: Token is null or empty");
                throw new AuthenticationCredentialsNotFoundException("Token is missing or empty.");
            }

            // Attempt to parse the token
            System.out.println("DEBUG: Parsing Token...");
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            System.out.println("DEBUG: Token validation successful.");
            return true;

        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            System.out.println("DEBUG: Token validation failed - Token Expired: " + ex.getMessage());
            throw new AuthenticationCredentialsNotFoundException("Token expired", ex);

        } catch (io.jsonwebtoken.MalformedJwtException ex) {
            System.out.println("DEBUG: Token validation failed - Malformed Token: " + ex.getMessage());
            throw new AuthenticationCredentialsNotFoundException("Malformed JWT token", ex);

        } catch (io.jsonwebtoken.SignatureException ex) {
            System.out.println("DEBUG: Token validation failed - Invalid Signature: " + ex.getMessage());
            throw new AuthenticationCredentialsNotFoundException("Invalid token signature", ex);

        } catch (Exception ex) {
            System.out.println("DEBUG: Token validation failed: " + ex.getMessage());
            throw new AuthenticationCredentialsNotFoundException("Invalid or expired JWT token", ex);
        }
    }


}
