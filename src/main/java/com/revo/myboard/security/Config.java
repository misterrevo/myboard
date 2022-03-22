package com.revo.myboard.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration("security")
@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
class Config extends WebSecurityConfigurerAdapter {

    private static final String EXCEPTION_MESSAGE = "Error while configurin HttpSecurity class, exception message: %s";

    private final AuthenticationFilter authenticationFilter;
    private final DetailsService detailsService;
    @Value("${spring.security.jwt.secret}")
    private String secret;

    @Override
    protected void configure(HttpSecurity http) {
        try {
            http.csrf().disable();
            http.cors();
            configureSecurity(http);
        } catch (Exception exception) {
            log.warn(EXCEPTION_MESSAGE.formatted(exception.getMessage()));
        }
    }

    private void configureSecurity(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(authenticationFilter)
                .addFilter(new TokenAuthorizationFilter(authenticationManager(), detailsService, secret))
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }

    @Bean
    public BCryptPasswordEncoder getBcryptPasswordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
