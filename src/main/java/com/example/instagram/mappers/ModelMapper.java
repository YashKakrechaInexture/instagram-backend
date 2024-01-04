package com.example.instagram.mappers;

import com.example.instagram.dto.response.UserProfileResponse;
import com.example.instagram.model.User;

public class ModelMapper {

    public static UserProfileResponse userToUserProfileResponse(User user){
        UserProfileResponse userProfileResponse = new UserProfileResponse();
        userProfileResponse.setEmail(user.getEmail());
        userProfileResponse.setUsername(user.getUsername());
        userProfileResponse.setFullName(user.getFullName());
        userProfileResponse.setDescription(user.getDescription());
        return userProfileResponse;
    }
}
