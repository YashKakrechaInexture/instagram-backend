package com.example.instagram.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User followerUser;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User followingToUser;
}
