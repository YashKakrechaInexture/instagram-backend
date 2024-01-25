package com.example.instagram.dto.response;

import lombok.Data;

@Data
public class PostResponse {
    private long id;
    private String image;
    private String caption;
    private long likes;
    private boolean likedByCurrentUser;
    private UserFollowDTO user;
}
