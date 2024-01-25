package com.example.instagram.dto.inputs;

import lombok.Data;
import lombok.NonNull;

@Data
public class PostInput {
    @NonNull
    private String caption;
}
