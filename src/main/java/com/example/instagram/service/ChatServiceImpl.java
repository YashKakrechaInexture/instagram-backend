package com.example.instagram.service;

import com.example.instagram.dto.enums.ImageType;
import com.example.instagram.dto.inputs.MessageInput;
import com.example.instagram.dto.inputs.ReadMessageInput;
import com.example.instagram.dto.response.ChatNotification;
import com.example.instagram.dto.response.ChatRoomDto;
import com.example.instagram.dto.response.ChatUnreadMessageCountResponse;
import com.example.instagram.dto.response.MessageDto;
import com.example.instagram.exception.ResourceNotFoundException;
import com.example.instagram.mappers.ModelMapper;
import com.example.instagram.model.ChatRoom;
import com.example.instagram.model.Message;
import com.example.instagram.model.User;
import com.example.instagram.model.enums.MessageStatus;
import com.example.instagram.repository.jpa.ChatRoomRepository;
import com.example.instagram.repository.mongo.MessageRepository;
import com.example.instagram.repository.jpa.UserRepository;
import com.example.instagram.security.util.JwtTokenUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ChatServiceImpl implements ChatService {
    private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);

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

    @Autowired
    private ImageService imageService;

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
                        .timestamp(message.getTimestamp())
                        .build()
        );
    }

    @Override
    public void readMessage(ReadMessageInput readMessageInput) {
        Message message = new Message();
        message.setId(readMessageInput.getId());
        message = messageRepository.findById(message.getId()).orElseThrow(() -> new ResourceNotFoundException("Message not found with id: "+readMessageInput.getId()));
        message.setStatus(MessageStatus.READ);
        messageRepository.save(message);
    }

    @Override
    @Transactional
    public List<MessageDto> getMessages(String recipient, String authorization) {
        User senderUser = getUserFromAuthorizationToken(authorization);
        User recipientUser = userRepository.findByUsername(recipient);
        ChatRoom chatRoom = findChatRoomFromSenderAndRecipient(senderUser, recipientUser, true);
        List<Message> messageList = messageRepository.findAllByChatId(chatRoom.getChatId());
        List<MessageDto> messageDtoList = new ArrayList<>();
        for(Message message : messageList){
            MessageDto messageDto = ModelMapper.messageToMessageDto(message);
            messageDtoList.add(messageDto);
            message.setStatus(MessageStatus.READ);
            messageRepository.save(message);
        }
        return messageDtoList;
    }

    @Override
    public List<ChatRoomDto> getChatRoomList(String authorization) {
        User recipientUser = getUserFromAuthorizationToken(authorization);
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByRecipient(recipientUser);
        List<ChatRoomDto> chatRoomDtoList = new ArrayList<>();
        for(ChatRoom chatRoom : chatRoomList){
            ChatRoomDto chatRoomDto = ModelMapper.chatRoomToChatRoomDto(chatRoom);
            chatRoomDto.getSender().setProfilePic(getBase64ImageFromImagePath(chatRoomDto.getSender().getProfilePic()));
            chatRoomDto.setLastMessage(
                    ModelMapper.messageToLastMessageDto(
                            messageRepository.findFirstByChatIdOrderByTimestampDesc(chatRoomDto.getChatId())
                    )
            );
            int unreadMessages = messageRepository.countBySenderAndRecipientAndStatus(chatRoomDto.getSender().getUsername(),
                    recipientUser.getUsername(), MessageStatus.DELIVERED);
            chatRoomDto.setUnreadMessages(unreadMessages);
            chatRoomDtoList.add(chatRoomDto);
        }
        Collections.sort(chatRoomDtoList, (o1, o2) -> o2.getLastMessage().getTimestamp().compareTo(o1.getLastMessage().getTimestamp()));
        return chatRoomDtoList;
    }

    @Override
    public ChatUnreadMessageCountResponse getChatUnreadMessageCount(String authorization) {
        User user = getUserFromAuthorizationToken(authorization);
        int unreadMessages = messageRepository.countByRecipientAndStatus(user.getUsername(), MessageStatus.DELIVERED);
        ChatUnreadMessageCountResponse chatUnreadMessageCountResponse = new ChatUnreadMessageCountResponse();
        chatUnreadMessageCountResponse.setUnreadMessages(unreadMessages);
        return chatUnreadMessageCountResponse;
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
        message.setStatus(MessageStatus.DELIVERED);
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

    private String getBase64ImageFromImagePath(String imageName){
        if(imageName!=null) {
            try {
                String base64Image = imageService.getBase64ImageByName(imageName, ImageType.PROFILE_PIC);
                if (base64Image != null) {
                    return "data:image/jpeg;base64," + base64Image;
                }
            } catch (IOException ioException) {
                logger.error(ioException.toString());
            }
        }
        return null;
    }
}
