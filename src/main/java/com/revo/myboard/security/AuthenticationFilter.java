package com.revo.myboard.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revo.myboard.security.dto.CredentialsDTO;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String PROCCESS_URL = "/login";
    private static final String AUTHENTICATION_EXCEPTION_MESSAGE = "Something went wrong while parsing /login request body";

    private final ObjectMapper objectMapper;

    public AuthenticationFilter(AuthenticationSuccessHandler successHandler, AuthManager authManager, ObjectMapper objectMapper, AuthenticationFailureHandler failureHandler) {
        this.objectMapper = objectMapper;
        this.setAuthenticationSuccessHandler(successHandler);
        this.setAuthenticationFailureHandler(failureHandler);
        this.setAuthenticationManager(authManager);
        this.setFilterProcessesUrl(PROCCESS_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            CredentialsDTO credentials = mapFromJson(request);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(credentials.getLogin(), credentials.getPassword());
            setDetails(request, token);
            return authenticate(token);
        } catch (IOException exception) {
            throw new InternalAuthenticationServiceException(AUTHENTICATION_EXCEPTION_MESSAGE, exception);
        }
    }

    private Authentication authenticate(UsernamePasswordAuthenticationToken token){
        return getAuthenticationManager().authenticate(token);
    }

    private CredentialsDTO mapFromJson(HttpServletRequest request) throws IOException {
        return objectMapper.readValue(request.getInputStream(), CredentialsDTO.class);
    }
}
