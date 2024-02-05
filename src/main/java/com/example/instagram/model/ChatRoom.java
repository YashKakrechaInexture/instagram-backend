package com.example.instagram.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String chatId;

    @OneToOne
    private User sender;

    @OneToOne
    private User recipient;
}
