package com.example.instagram.controller;

import com.example.instagram.dto.inputs.MessageInput;
import com.example.instagram.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat")
    public void processMessage(@Payload MessageInput messageInput) {
        chatService.processMessage(messageInput);
    }
}
