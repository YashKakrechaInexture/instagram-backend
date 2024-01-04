package com.example.instagram.service;

import com.example.instagram.dto.inputs.UserInput;
import com.example.instagram.dto.response.UserProfileResponse;
import com.example.instagram.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User getUserById(Long id);
    User saveUser(UserInput userInput);
    UserProfileResponse getUserProfile(String authorization);
}
