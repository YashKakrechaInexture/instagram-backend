package com.example.instagram.dto.inputs;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserInput {
    @NonNull
    private String email;
    @NonNull
    private String username;
    @NonNull
    private String password;
}
