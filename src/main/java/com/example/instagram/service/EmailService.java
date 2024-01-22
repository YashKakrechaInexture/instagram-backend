package com.example.instagram.service;

import com.example.instagram.dto.internal.EmailDetails;

public interface EmailService {
    String sendMail(EmailDetails emailDetails);
}
