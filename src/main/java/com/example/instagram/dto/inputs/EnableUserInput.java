package com.example.instagram.dto.inputs;

import lombok.Data;
import lombok.NonNull;

@Data
public class EnableUserInput {
    @NonNull
    private String email;
    @NonNull
    private String otp;
}
