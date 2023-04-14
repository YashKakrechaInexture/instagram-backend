package com.example.instagram.service;

import com.example.instagram.dto.UserDto;
import com.example.instagram.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User getUserById(Long id);
    User saveUser(UserDto userDto, String authorization);
}
