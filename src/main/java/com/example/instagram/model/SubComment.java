package com.example.instagram.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class SubComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
}
