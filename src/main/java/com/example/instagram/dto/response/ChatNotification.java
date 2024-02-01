package com.example.instagram.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatNotification {
    private long id;
    private String chatID;
    private String sender;
    private String recipient;
}
