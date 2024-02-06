package com.example.instagram.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class LastMessageDto {
    private String sender;
    private String message;
    private Date timestamp;
}
