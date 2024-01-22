package com.example.instagram.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private User followerUser;

    @OneToOne
    private User followingToUser;
}
