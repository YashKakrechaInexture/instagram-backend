package com.example.instagram.service;

import com.example.instagram.dto.inputs.MessageInput;

public interface ChatService {
    void processMessage(MessageInput messageInput);
}
