package com.example.instagram.dto.response;

import lombok.Data;

@Data
public class UserProfileResponse {
    private String email;
    private String username;
    private String fullName;
    private String description;
    private String profilePic;
    private long postCount;
    private long followers;
    private long following;
    private boolean selfUser;
    private boolean followedThisUser;
    private boolean verified;
}
