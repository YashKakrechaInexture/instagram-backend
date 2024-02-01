package com.example.instagram.service;

import com.example.instagram.dto.inputs.EnableUserInput;
import com.example.instagram.dto.inputs.UserSignupInput;
import com.example.instagram.dto.response.*;
import com.example.instagram.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User getUserById(Long id);
    ResponseMessage saveUser(UserSignupInput userSignupInput);
    ResponseMessage enableUser(EnableUserInput enableUserInput);
    UserProfileResponse getUserProfile(String username, String authorization);
    MessagePageProfileResponse getMessagePageProfile(String username);
    ResponseMessage updateProfilePic(MultipartFile profilePic, String authorization);
    List<SearchUserResponse> searchUserByUsername(String searchUsername, String authorization);
    ResponseMessage followUser(String followUsername, String authorization);
    ResponseMessage unfollowUser(String followUsername, String authorization);
    List<UserFollowDTO> followersList(String username, String authorization);
    List<UserFollowDTO> followingList(String username, String authorization);
}
