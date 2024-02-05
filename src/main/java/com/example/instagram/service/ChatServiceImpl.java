package com.example.instagram.service;

import com.example.instagram.dto.inputs.MessageInput;
import com.example.instagram.dto.response.ChatNotification;
import com.example.instagram.exception.ResourceNotFoundException;
import com.example.instagram.model.ChatRoom;
import com.example.instagram.model.Message;
import com.example.instagram.model.User;
import com.example.instagram.repository.jpa.ChatRoomRepository;
import com.example.instagram.repository.mongo.MessageRepository;
import com.example.instagram.repository.jpa.UserRepository;
import com.example.instagram.security.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public void processMessage(MessageInput messageInput) {
        Message message = fetchMessageFromMessageInput(messageInput);
        messageRepository.save(message);
        messagingTemplate.convertAndSendToUser(
                messageInput.getRecipient(),
                "/queue/message",
                ChatNotification.builder()
                        .id(message.getId())
                        .chatID(message.getChatId())
                        .sender(message.getSender())
                        .recipient(message.getRecipient())
                        .message(message.getMessage())
                        .build()
        );
    }

    @Override
    public List<Message> getMessages(String recipient, String authorization) {
        User senderUser = getUserFromAuthorizationToken(authorization);
        User recipientUser = userRepository.findByUsername(recipient);
        ChatRoom chatRoom = findChatRoomFromSenderAndRecipient(senderUser, recipientUser, true);
        return messageRepository.findAllByChatId(chatRoom.getChatId());
    }

    private Message fetchMessageFromMessageInput(MessageInput messageInput){
//        todo : find sender from JWT token, first check "is JWT token coming in headers?" then implement below part
        User sender = userRepository.findByUsername(messageInput.getSender());
        User recipient = userRepository.findByUsername(messageInput.getRecipient());
        ChatRoom chatRoom = findChatRoomFromSenderAndRecipient(sender, recipient, true);

        Message message = new Message();
        message.setChatId(chatRoom.getChatId());
        message.setSender(sender.getUsername());
        message.setRecipient(recipient.getUsername());
        message.setMessage(messageInput.getMessage());
        message.setTimestamp(new Date());
        return message;
    }

    private ChatRoom findChatRoomFromSenderAndRecipient(User sender, User recipient, boolean createChatRoomIfNotExist){
        ChatRoom chatRoom = chatRoomRepository.findBySenderAndRecipient(sender,recipient);
        if(chatRoom==null && createChatRoomIfNotExist){
            String chatId = UUID.randomUUID().toString();

            chatRoom = new ChatRoom();
            chatRoom.setChatId(chatId);
            chatRoom.setSender(sender);
            chatRoom.setRecipient(recipient);
            chatRoomRepository.save(chatRoom);

            ChatRoom recipientChatRoom = new ChatRoom();
            recipientChatRoom.setChatId(chatId);
            recipientChatRoom.setSender(recipient);
            recipientChatRoom.setRecipient(sender);
            chatRoomRepository.save(recipientChatRoom);
        }
        return chatRoom;
    }

    private long getUserIdFromAuthorizationToken(String authorization){
        String strId = jwtTokenUtil.extractClaimValue(authorization, JwtTokenUtil.JWT_ID);
        return Long.parseLong(strId);
    }
    private User getUserFromAuthorizationToken(String authorization){
        long id = getUserIdFromAuthorizationToken(authorization);
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+id));
    }
}
