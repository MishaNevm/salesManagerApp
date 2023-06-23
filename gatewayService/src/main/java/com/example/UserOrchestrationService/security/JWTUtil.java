package com.example.UserOrchestrationService.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {

    private final String secret;

    public JWTUtil(@Value("${secret}") String secret) {
        this.secret = secret;
    }

    public String generateToken(String username) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusHours(20).toInstant());
        return JWT.create().withSubject("User details")
                .withClaim("email", username)
                .withIssuedAt(new Date())
                .withIssuer("Misha")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveClaim(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("Misha")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("email").asString();
    }
}