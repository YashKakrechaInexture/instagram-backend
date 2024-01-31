package com.example.instagram.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String chatId;

    @OneToOne
    private User sender;

    @OneToOne
    private User recipient;

    @Column(length = 10000)
    private String message;

    private Date timestamp;
}
