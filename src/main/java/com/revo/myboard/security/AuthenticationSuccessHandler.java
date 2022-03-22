package com.revo.myboard.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;

@Component
class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String HEADER_NAME = "Authorization";
    private static final String HEADER_VALUE = "Bearer %s";

    @Value("${spring.security.jwt.secret}")
    private String secret;
    @Value("${spring.security.jwt.expirationTime}")
    private long expirationTime;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        String token = createToken(authentication);
        response.setHeader(HEADER_NAME, HEADER_VALUE.formatted(token));
    }

    private String createToken(Authentication authentication){
        var principal = authentication.getPrincipal();
        return JWT.create()
                .withSubject(principal.toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC256(secret));
    }
}
