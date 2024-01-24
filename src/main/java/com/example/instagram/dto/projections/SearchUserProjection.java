package com.example.instagram.dto.projections;

public interface SearchUserProjection {
    long getId();
    String getUsername();
    String getFullName();
    String getProfilePic();
    boolean isVerified();
}
