package io.valentinsoare.bloggingengineapi.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.valentinsoare.bloggingengineapi.exception.BloggingEngineException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
@NoArgsConstructor
public class JwtTokenProvider {
    @Value("${app.jwt.secret}")
    @NotBlank(message = "Secret key is mandatory!")
    @Size(min = 32, max = 64, message = "Secret key must be between 32 and 64 characters!")
    private String secret;

    @Value("${app.jwt.expiration}")
    @NotBlank(message = "Expiration length is mandatory!")
    @Min(value = 86400000, message = "Expiration length must be greater than 1 day in milliseconds!")
    private long expirationDate;

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + this.expirationDate);

        return Jwts.builder()
                .subject(username)
                .issuedAt(currentDate)
                .expiration(expirationDate)
                .signWith(getSecretKey())
                .compact();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secret));
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();

            return true;
        } catch (MalformedJwtException e) {
            throw new BloggingEngineException("validateToken", "Invalid JWT Token", Map.of("token", token));
        } catch (UnsupportedJwtException e) {
            throw new BloggingEngineException("validateToken", "Unsupported JWT Token", Map.of("token", token));
        } catch (ExpiredJwtException e) {
            throw new BloggingEngineException("validateToken", "Expired JWT Token", Map.of("token", token));
        } catch (IllegalArgumentException e) {
            throw new BloggingEngineException("validateToken", "JWT claims string is null or empty", Map.of("token", "nullOrEmpty"));
        }
    }
}
