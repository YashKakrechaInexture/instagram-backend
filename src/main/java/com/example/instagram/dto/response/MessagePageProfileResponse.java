package com.example.instagram.dto.response;

import lombok.Data;

@Data
public class MessagePageProfileResponse {
    private String username;
    private boolean verified;
    private String profilePic;
}
