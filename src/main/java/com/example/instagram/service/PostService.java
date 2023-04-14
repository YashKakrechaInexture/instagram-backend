package com.example.instagram.service;

import com.example.instagram.model.Post;

import java.util.List;

public interface PostService {
    List<Post> getAll(String status);
    Post getPostById(Long id);
    Post savePost(Post post, String authorization);
}
