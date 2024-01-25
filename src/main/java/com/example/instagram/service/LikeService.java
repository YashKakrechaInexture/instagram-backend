package com.example.instagram.service;

import com.example.instagram.dto.response.ResponseMessage;
import com.example.instagram.model.Post;
import org.springframework.http.ResponseEntity;

public interface LikeService {
    ResponseMessage likePost(Post post, String authorization);
    ResponseMessage unlikePost(Post post, String authorization);
}
