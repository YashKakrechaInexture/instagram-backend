package com.example.instagram.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_like")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private User user;

    @OneToOne
    private Post post;
}
