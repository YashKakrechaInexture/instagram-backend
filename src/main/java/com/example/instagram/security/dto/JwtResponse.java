package com.example.instagram.security.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class JwtResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -8091879091924046844L;
    private final String token;
    private final String username;
}