package com.example.instagram.service;

import com.example.instagram.dto.inputs.MessageInput;
import com.example.instagram.dto.inputs.ReadMessageInput;
import com.example.instagram.dto.response.ChatRoomDto;
import com.example.instagram.dto.response.ChatUnreadMessageCountResponse;
import com.example.instagram.dto.response.MessageDto;
import com.example.instagram.model.Message;

import java.util.List;

public interface ChatService {
    void processMessage(MessageInput messageInput);
    void readMessage(ReadMessageInput readMessageInput);
    List<MessageDto> getMessages(String recipient, String authorization);
    List<ChatRoomDto> getChatRoomList(String authorization);
    ChatUnreadMessageCountResponse getChatUnreadMessageCount(String authorization);
}
