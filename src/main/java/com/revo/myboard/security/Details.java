package com.revo.myboard.security;

import com.revo.myboard.group.Authority;
import com.revo.myboard.user.User;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.stream.Stream;

@Builder
class Details implements UserDetails {

    @Serial
    private static final long serialVersionUID = 7502322939676542596L;
    private static final String ROLE_PREFIX = "ROLE_";
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return mapFromUser(user.getGroup().getAuthority());
    }

    private Collection<? extends GrantedAuthority> mapFromUser(Authority authority){
        return Stream.of(authority).map(n -> new SimpleGrantedAuthority(ROLE_PREFIX + n)).toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isBlocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}
