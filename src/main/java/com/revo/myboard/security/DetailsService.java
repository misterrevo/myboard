package com.revo.myboard.security;

import com.revo.myboard.exception.UserNotExistsException;
import com.revo.myboard.user.User;
import com.revo.myboard.user.UserServiceApi;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class DetailsService implements UserDetailsService {

    private final UserServiceApi userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return buildDetails(username);
    }

    private UserDetails buildDetails(String username){
        return Details.builder().user(getUser(username)).build();
    }

    private User getUser(String username){
        try{
            return userService.getUserByLogin(username);
        } catch (UserNotExistsException exception){
            throw new UsernameNotFoundException(username);
        }
    }
}
