package com.example.instagram.mappers;

import com.example.instagram.dto.projections.SearchUserProjection;
import com.example.instagram.dto.response.SearchUserResponse;
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

    public static SearchUserResponse searchUserProjectionToSearchUserResponse(SearchUserProjection searchUserProjection){
        SearchUserResponse searchUserResponse = new SearchUserResponse();
        searchUserResponse.setUsername(searchUserProjection.getUsername());
        searchUserResponse.setFullName(searchUserProjection.getFullName());
        return searchUserResponse;
    }
}
