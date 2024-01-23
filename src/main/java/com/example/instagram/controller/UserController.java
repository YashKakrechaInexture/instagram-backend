package com.example.instagram.controller;

import com.example.instagram.dto.inputs.EnableUserInput;
import com.example.instagram.dto.inputs.UserSignupInput;
import com.example.instagram.dto.response.ResponseMessage;
import com.example.instagram.dto.response.SearchUserResponse;
import com.example.instagram.dto.response.UserFollowDTO;
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
    public ResponseEntity<ResponseMessage> createOrUpdateUser(@RequestBody UserSignupInput userSignupInput){
        return ResponseEntity.ok(userService.saveUser(userSignupInput));
    }

    @PostMapping("/enable")
    public ResponseEntity<ResponseMessage> enableUser(@RequestBody EnableUserInput enableUserInput){
        return ResponseEntity.ok(userService.enableUser(enableUserInput));
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

    @GetMapping("/searchUser")
    public ResponseEntity<List<SearchUserResponse>> searchUserByUsername(@RequestParam("username") String username,
                                                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        return ResponseEntity.ok(userService.searchUserByUsername(username, authorization));
    }

    @PostMapping("/follow")
    public ResponseEntity<ResponseMessage> followUser(@RequestParam("followUsername") String followUsername,
                                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        return ResponseEntity.ok(userService.followUser(followUsername, authorization));
    }

    @PostMapping("/unfollow")
    public ResponseEntity<ResponseMessage> unfollowUser(@RequestParam("followUsername") String followUsername,
                                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        return ResponseEntity.ok(userService.unfollowUser(followUsername, authorization));
    }

    @GetMapping("/followersList")
    public ResponseEntity<List<UserFollowDTO>> followersList(@RequestParam("username") String username,
                                                             @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        return ResponseEntity.ok(userService.followersList(username, authorization));
    }

    @GetMapping("/followingList")
    public ResponseEntity<List<UserFollowDTO>> followingList(@RequestParam("username") String username,
                                                             @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        return ResponseEntity.ok(userService.followingList(username, authorization));
    }
}
