package com.example.instagram.dto.response;

import com.example.instagram.model.enums.MessageStatus;
import lombok.Data;

import java.util.Date;

@Data
public class MessageDto {
    private String id;
    private String sender;
    private String recipient;
    private String message;
    private Date timestamp;
    private MessageStatus status;
}
