package com.example.scm.Services.Implementations;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import com.example.scm.Reposititers.UserRepo;

@Service
public class SecurityCustomUserDetailsService implements UserDetailsService{

    
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
          
        // apne user ko load karna hai
        // user ko database se load karna hai
       return  userRepo.findByEmail(username)
        .orElseThrow(()->  new UsernameNotFoundException("User not found with email: " + username));

        //throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
    }

}
