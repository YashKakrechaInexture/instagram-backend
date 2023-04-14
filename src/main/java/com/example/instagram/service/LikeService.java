package com.example.instagram.service;

import com.example.instagram.model.Post;
import org.springframework.http.ResponseEntity;

public interface LikeService {
    ResponseEntity<?> likePost(Post post, String authorization);
    ResponseEntity<?> unlikePost(Post post, String authorization);
}
