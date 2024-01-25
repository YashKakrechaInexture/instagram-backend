package com.example.instagram.mappers;

import com.example.instagram.dto.inputs.PostInput;
import com.example.instagram.dto.projections.SearchUserProjection;
import com.example.instagram.dto.response.PostResponse;
import com.example.instagram.dto.response.SearchUserResponse;
import com.example.instagram.dto.response.UserFollowDTO;
import com.example.instagram.dto.response.UserProfileResponse;
import com.example.instagram.model.Post;
import com.example.instagram.model.User;

public class ModelMapper {

    public static UserProfileResponse userToUserProfileResponse(User user){
        UserProfileResponse userProfileResponse = new UserProfileResponse();
        userProfileResponse.setEmail(user.getEmail());
        userProfileResponse.setUsername(user.getUsername());
        userProfileResponse.setFullName(user.getFullName());
        userProfileResponse.setDescription(user.getDescription());
        userProfileResponse.setVerified(user.isVerified());
        return userProfileResponse;
    }

    public static SearchUserResponse searchUserProjectionToSearchUserResponse(SearchUserProjection searchUserProjection){
        SearchUserResponse searchUserResponse = new SearchUserResponse();
        searchUserResponse.setUsername(searchUserProjection.getUsername());
        searchUserResponse.setFullName(searchUserProjection.getFullName());
        searchUserResponse.setVerified(searchUserProjection.isVerified());
        return searchUserResponse;
    }

    public static UserFollowDTO userToUserFollowDTO(User user){
        UserFollowDTO userFollowDTO = new UserFollowDTO();
        userFollowDTO.setUsername(user.getUsername());
        userFollowDTO.setFullName(user.getFullName());
        userFollowDTO.setProfilePic(user.getProfilePic());
        userFollowDTO.setVerified(user.isVerified());
        return userFollowDTO;
    }

    public static Post postInputToPost(PostInput postInput){
        Post post = new Post();
        post.setCaption(postInput.getCaption());
        return post;
    }

    public static PostResponse postToPostResponse(Post post){
        PostResponse postResponse = new PostResponse();
        postResponse.setId(post.getId());
        postResponse.setCaption(post.getCaption());
        postResponse.setUser(userToUserFollowDTO(post.getUser()));
        return postResponse;
    }
}
