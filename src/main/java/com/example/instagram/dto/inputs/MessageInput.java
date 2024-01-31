package com.example.instagram.dto.inputs;

import lombok.Data;

@Data
public class MessageInput {
    private String recipient;
    private String message;
}
