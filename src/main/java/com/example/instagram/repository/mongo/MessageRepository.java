package com.example.instagram.repository.mongo;

import com.example.instagram.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message,Long> {

    List<Message> findAllByChatId(String chatId);
}
