package com.example.instagram.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ChatNotification {
    private String id;
    private String chatID;
    private String sender;
    private String recipient;
    private String message;
    private Date timestamp;
}
