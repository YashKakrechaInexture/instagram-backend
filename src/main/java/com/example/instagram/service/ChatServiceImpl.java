package com.example.instagram.service;

import com.example.instagram.dto.inputs.MessageInput;
import com.example.instagram.model.ChatRoom;
import com.example.instagram.model.Message;
import com.example.instagram.model.User;
import com.example.instagram.repository.ChatRoomRepository;
import com.example.instagram.repository.MessageRepository;
import com.example.instagram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    @Override
    public void processMessage(MessageInput messageInput) {
        Message message = fetchMessageFromMessageInput(messageInput);
        messageRepository.save(message);
        messagingTemplate.convertAndSendToUser(
                messageInput.getRecipient(),
                "/queue/message",
                message
        );
    }

    private Message fetchMessageFromMessageInput(MessageInput messageInput){
//        todo : find sender from JWT token, first check "is JWT token coming in headers?" then implement below part
//        User sender = userRepository.findByUsername(messageInput.getSender());
        User sender = null;
        User recipient = userRepository.findByUsername(messageInput.getRecipient());
//        todo : find chatroom id from sender & recipient
//        ChatRoom chatRoom = chatRoomRepository.findBySenderAndAndRecipient(sender,recipient); - 1st priority
//        ChatRoom chatRoom = chatRoomRepository.findById(messageInput.getChatRoomId())  - 2nd priority
//                .orElseThrow(() -> new RuntimeException("ChatRoom not found!"));
        ChatRoom chatRoom = null;

        Message message = new Message();
        message.setChatId(chatRoom.getChatId());
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setMessage(messageInput.getMessage());
        message.setTimestamp(new Date());
        return message;
    }
}
