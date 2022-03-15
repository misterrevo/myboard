package com.revo.myboard.security;

import com.revo.myboard.user.User;
import com.revo.myboard.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
 * Created By Revo
 */

@Service
@AllArgsConstructor
public class DetailsService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return buildDetails(username);
    }

    private UserDetails buildDetails(String username){
        return Details.builder().user(getUser(username)).build();
    }

    private User getUser(String username){
        return repository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

}
