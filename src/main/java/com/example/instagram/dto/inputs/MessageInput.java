package com.example.instagram.dto.inputs;

import lombok.Data;

@Data
public class MessageInput {
    private String sender;
    private String recipient;
    private String message;
}
