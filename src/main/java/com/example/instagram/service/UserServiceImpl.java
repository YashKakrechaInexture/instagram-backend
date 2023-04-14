package com.example.instagram.service;

import com.example.instagram.dto.UserDto;
import com.example.instagram.exception.ResourceNotFoundException;
import com.example.instagram.model.User;
import com.example.instagram.repository.UserRepository;
import com.example.instagram.security.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id:"+id));
    }

    @Override
    public User saveUser(UserDto userDto, String authorization) {
        if(!Boolean.parseBoolean(jwtTokenUtil.getClaimFromToken(authorization.substring(7), claims -> claims.get(JwtTokenUtil.JWT_ROLE)).toString())){
            throw new RuntimeException("You do not have admin permissions!");
        }
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setAdmin(false);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(user);
    }
}
