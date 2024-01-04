package com.example.instagram.controller;

import com.example.instagram.model.Post;
import com.example.instagram.model.User;
import com.example.instagram.service.LikeService;
import com.example.instagram.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private LikeService likeService;

    @GetMapping
    public List<Post> getAllPosts(@RequestParam(required = false) String status) {
        return postService.getAll(status);
    }

    @GetMapping("{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id){
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PostMapping
    public ResponseEntity<Post> createOrUpdatePost(@RequestBody Post post,
                                                   @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        return ResponseEntity.ok(postService.savePost(post,authorization));
    }

    @PostMapping("/like")
    public ResponseEntity<?> likePost(@RequestBody Post post,
                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        return likeService.likePost(post,authorization);
    }

    @PostMapping("/unlike")
    public ResponseEntity<?> unlikePost(@RequestBody Post post,
                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        return likeService.unlikePost(post,authorization);
    }
}
