package com.example.instagram.model;

import com.example.instagram.model.enums.MessageStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
public class Message {
    @Id
    private String id;

    private String chatId;

    private String sender;

    private String recipient;

    private String message;

    private Date timestamp;

    private MessageStatus status;
}
