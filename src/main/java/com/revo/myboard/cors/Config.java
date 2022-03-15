package com.revo.myboard.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration("cors")
@EnableWebMvc
public class Config implements WebMvcConfigurer {

    private static final String MAPPING_PATH = "/**";
    private static final String ALLOWED_ORIGINS = "*";
    private static final String ALLOWED_HEADERS = "*";
    private static final String[] EXPOSED_HEADERS = {"Authorization"};
    private static final String ALLOWED_METHODS = "*";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(MAPPING_PATH).allowedOrigins(ALLOWED_ORIGINS).allowedHeaders(ALLOWED_HEADERS).exposedHeaders(EXPOSED_HEADERS).allowedMethods(ALLOWED_METHODS);
    }

    @Bean
    public StrictHttpFirewall firewall() {
        var firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedDoubleSlash(true);
        return firewall;
    }

}
