package com.example.instagram.controller;

import com.example.instagram.dto.inputs.PostInput;
import com.example.instagram.dto.response.PostResponse;
import com.example.instagram.dto.response.ResponseMessage;
import com.example.instagram.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/post")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public List<PostResponse> getAllPosts(@RequestParam String username) {
        return postService.getAllPostsByUsername(username);
    }

    @GetMapping("{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        return ResponseEntity.ok(postService.getPostById(id, authorization));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponse> createPost(@RequestParam String caption,
                                                   @RequestParam("post-pic") MultipartFile postPic,
                                                   @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws IOException {
        PostInput postInput = new PostInput(caption);
        return ResponseEntity.ok(postService.createPost(postInput,postPic,authorization));
    }

    @PostMapping("/like")
    public ResponseMessage likePost(@RequestParam long postId,
                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        return postService.likePost(postId,authorization);
    }

    @PostMapping("/unlike")
    public ResponseMessage unlikePost(@RequestParam long postId,
                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        return postService.unlikePost(postId,authorization);
    }
}
