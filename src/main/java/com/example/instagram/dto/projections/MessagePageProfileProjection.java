package com.example.instagram.dto.projections;

public interface MessagePageProfileProjection {
    String getUsername();
    boolean isVerified();
    String getProfilePic();
}
