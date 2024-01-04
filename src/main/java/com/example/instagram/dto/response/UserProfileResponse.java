package com.example.instagram.dto.response;

import lombok.Data;

@Data
public class UserProfileResponse {
    private String email;
    private String username;
    private String fullName;
    private String description;
//    private String image; //todo
    private long postCount;
    private long followers;
    private long following;
}