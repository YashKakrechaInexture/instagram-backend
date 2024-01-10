package com.example.instagram.controller;

import com.example.instagram.dto.inputs.UserInput;
import com.example.instagram.dto.response.ResponseMessage;
import com.example.instagram.dto.response.UserProfileResponse;
import com.example.instagram.model.User;
import com.example.instagram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> createOrUpdateUser(@RequestBody UserInput userInput){
        return ResponseEntity.ok(userService.saveUser(userInput));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        return ResponseEntity.ok(userService.getUserProfile(authorization));
    }

    @PutMapping("/profile-pic")
    public ResponseEntity<ResponseMessage> updateProfilePic(@RequestParam("profile-pic") MultipartFile profilePic,
                                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        return ResponseEntity.ok(userService.updateProfilePic(profilePic, authorization));
    }
}
