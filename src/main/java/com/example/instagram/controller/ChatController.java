package com.example.instagram.controller;

import com.example.instagram.dto.inputs.MessageInput;
import com.example.instagram.dto.inputs.ReadMessageInput;
import com.example.instagram.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat")
    public void processMessage(@Payload MessageInput messageInput) {
        chatService.processMessage(messageInput);
    }

    @MessageMapping("/chat/read")
    public void readMessage(@Payload ReadMessageInput readMessageInput) {
        chatService.readMessage(readMessageInput);
    }

    @GetMapping("/chat/{recipient}")
    public ResponseEntity<?> getMessages(@PathVariable String recipient,
                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        return ResponseEntity.ok(chatService.getMessages(recipient, authorization));
    }

    @GetMapping("/chat/room-list")
    public ResponseEntity<?> getChatRoomList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        return ResponseEntity.ok(chatService.getChatRoomList(authorization));
    }

    @GetMapping("/chat/unread-message-count")
    public ResponseEntity<?> getChatUnreadMessageCount(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        return ResponseEntity.ok(chatService.getChatUnreadMessageCount(authorization));
    }
}
