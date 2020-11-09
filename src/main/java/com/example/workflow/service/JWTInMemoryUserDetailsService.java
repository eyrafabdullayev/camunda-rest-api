package com.example.workflow.service;

import com.example.workflow.entity.JWTUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JWTInMemoryUserDetailsService implements UserDetailsService {

    static List<UserDetails> inMemoryUserDetailsList = new ArrayList<>();

    static {
        inMemoryUserDetailsList.add(new JWTUserDetails(1L, "admin",
                "$2a$10$GSJ8Ai2h89GYYdlA/GW/pe3.Nl4ZqonygMGKzcdlITZozHa492Qq2","ROLE_ADMIN"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDetails> userDetailsOptional = inMemoryUserDetailsList.stream()
                .filter(userDetails -> userDetails.getUsername().equals(username))
                .findFirst();

        if(!userDetailsOptional.isPresent())
            throw new UsernameNotFoundException(String.format("USER_NOT_FOUND '%s'", username));

        return userDetailsOptional.get();
    }
}
