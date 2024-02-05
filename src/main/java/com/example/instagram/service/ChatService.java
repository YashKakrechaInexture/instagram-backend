package com.example.instagram.service;

import com.example.instagram.dto.inputs.MessageInput;
import com.example.instagram.model.Message;

import java.util.List;

public interface ChatService {
    void processMessage(MessageInput messageInput);
    List<Message> getMessages(String recipient, String authorization);
}
