package com.revo.myboard.security;

import com.revo.myboard.exception.NonActiveAccountException;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__(@Lazy))
class AuthManager implements AuthenticationManager {

    private static final String MISS_AUTH_PARAMS = "You miss authentiaction params!";
    private static final String PASSWORD_NOT_MATCH = "Password not match!";
    private static final String BLOCKED = "User is blocked!";

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        UserDetails details = loadUser(auth);
        if (auth.getPrincipal() == null || auth.getCredentials() == null) {
            throw new BadCredentialsException(MISS_AUTH_PARAMS);
        }
        if (!encoder.matches(auth.getCredentials().toString(), details.getPassword())) {
            throw new BadCredentialsException(PASSWORD_NOT_MATCH);
        }
        if (!details.isAccountNonLocked()) {
            throw new BadCredentialsException(BLOCKED);
        }
        if (!details.isEnabled()) {
            throw new NonActiveAccountException(auth.getPrincipal().toString());
        }
        return new UsernamePasswordAuthenticationToken(details.getUsername(), null, details.getAuthorities());
    }

    private UserDetails loadUser(Authentication authentication){
        return userDetailsService.loadUserByUsername(authentication.getPrincipal().toString());
    }
}
