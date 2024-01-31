package com.example.instagram.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ChatRoom {
    @Id
    private String id;

    private String chatId;

    @OneToOne
    private User sender;

    @OneToOne
    private User recipient;
}
