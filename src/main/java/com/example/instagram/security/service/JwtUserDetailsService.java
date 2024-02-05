package com.example.instagram.security.service;

import com.example.instagram.model.User;
import com.example.instagram.repository.jpa.UserRepository;
import com.example.instagram.security.dto.JwtUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndEnabled(email,true);
        if(user==null){
            throw new UsernameNotFoundException("User not found with username: " + email);
        }
        JwtUserDetails userDetails = new JwtUserDetails();
        userDetails.setId(user.getId());
        userDetails.setEmail(user.getEmail());
        userDetails.setUsername(user.getUsername());
        userDetails.setPassword(user.getPassword());
        userDetails.setAdmin(user.isAdmin());
        userDetails.setEnabled(user.isEnabled());
        userDetails.setAuthorities(new HashSet<>());
        return userDetails;
    }
}