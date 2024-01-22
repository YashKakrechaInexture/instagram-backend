package com.example.instagram.dto.internal;

import lombok.Data;

@Data
public class EmailDetails {
    private String recipient;
    private String subject;
    private String msgBody;
}