package com.daniel.authenticationapi.infra.services;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Service
public class JWTService {

    @Value("${public.key}")
    private RSAPublicKey keyPub;
    @Value("${private.key}")
    private RSAPrivateKey keyPriv;

    public String generateJWT(Authentication authentication) {
        try {
            var algorithm = Algorithm.RSA256(keyPub, keyPriv);
            String claims = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
            String token = JWT.create()
                    .withIssuer("authentication-api")
                    .withSubject(authentication.getName())
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(LocalDateTime.now().plusHours(1L).toInstant(ZoneOffset.of("-03:00")))
                    .withClaim("scopes", claims)
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error generating token");
        }
    }

    public String getSubject(String token) {
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = Algorithm.RSA256(keyPub, keyPriv);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer("authentication-api").build();
            return verifier.verify(token).getSubject();
        } catch (JWTVerificationException exception){
            throw new RuntimeException("Invalid Token");
        }
    }
}
