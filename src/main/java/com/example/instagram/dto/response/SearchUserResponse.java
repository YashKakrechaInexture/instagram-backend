package com.example.instagram.dto.response;

import lombok.Data;

@Data
public class SearchUserResponse {
    private String username;
    private String fullName;
    private String profilePic;
    private boolean following;
    private boolean verified;
}
