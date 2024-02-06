package com.example.instagram.dto.response;

import lombok.Data;

@Data
public class ChatRoomDto {
    private String chatId;
    private ChatUserResponse sender;
    private LastMessageDto lastMessage;
    private int unreadMessages;
}
