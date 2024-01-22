package com.example.instagram.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    private String fullName;

    @JsonIgnore
    private String password;

    private boolean admin;

    private String description;

    private String profilePic;

    private boolean enabled;

    private String otp;
}
