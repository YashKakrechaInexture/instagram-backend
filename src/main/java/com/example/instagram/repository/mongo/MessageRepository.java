package com.example.instagram.repository.mongo;

import com.example.instagram.model.Message;
import com.example.instagram.model.enums.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message,String> {

    List<Message> findAllByChatId(String chatId);

    Message findFirstByChatIdOrderByTimestampDesc(String chatId);

    Integer countByRecipientAndStatus(String recipient, MessageStatus status);
    Integer countBySenderAndRecipientAndStatus(String sender, String recipient, MessageStatus status);
}
