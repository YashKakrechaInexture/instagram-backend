package com.example.instagram.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessagePageProfileResponse implements Serializable {
    private String username;
    private boolean verified;
    private String profilePic;
}
