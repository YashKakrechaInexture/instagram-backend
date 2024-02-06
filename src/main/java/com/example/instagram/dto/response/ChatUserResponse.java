package com.example.instagram.dto.response;

import lombok.Data;

@Data
public class ChatUserResponse {
    private String username;
    private String fullName;
    private String profilePic;
    private boolean verified;
}
