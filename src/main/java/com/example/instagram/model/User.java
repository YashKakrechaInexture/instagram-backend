package com.example.instagram.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_table", indexes = {
        @Index(name="idx_email", columnList = "email"),
        @Index(name="idx_username", columnList = "username"),
        @Index(name="idx_email_enabled", columnList = "email,enabled"),
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, length = 50)
    private String email;

    @Column(unique = true, length = 50)
    private String username;

    private String fullName;

    @JsonIgnore
    private String password;

    private boolean admin;

    private String description;

    private String profilePic;

    private boolean enabled;

    private String otp;

    private boolean verified;
}
