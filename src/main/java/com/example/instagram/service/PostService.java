package com.example.instagram.service;

import com.example.instagram.dto.inputs.PostInput;
import com.example.instagram.dto.response.PostResponse;
import com.example.instagram.dto.response.ResponseMessage;
import com.example.instagram.model.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    List<PostResponse> getAllPostsByUsername(String username);
    PostResponse getPostById(Long id, String authorization);
    PostResponse createPost(PostInput postInput, MultipartFile postPic, String authorization) throws IOException;
    ResponseMessage likePost(long postId, String authorization);
    ResponseMessage unlikePost(long postId, String authorization);
}
