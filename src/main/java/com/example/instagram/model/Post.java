package com.example.instagram.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String imageName;

    @Column(length = 1000)
    private String caption;

    @OneToOne
    private User user;

    @OneToMany
    private List<Comment> commentList;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createDateTime;
}
