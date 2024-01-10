package com.example.instagram.service;

import com.example.instagram.dto.inputs.UserInput;
import com.example.instagram.dto.response.ResponseMessage;
import com.example.instagram.dto.response.UserProfileResponse;
import com.example.instagram.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User getUserById(Long id);
    ResponseMessage saveUser(UserInput userInput);
    UserProfileResponse getUserProfile(String authorization);
    ResponseMessage updateProfilePic(MultipartFile profilePic, String authorization);
}
