package com.example.instagram.service;

import com.example.instagram.exception.ResourceNotFoundException;
import com.example.instagram.model.Post;
import com.example.instagram.model.User;
import com.example.instagram.repository.PostRepository;
import com.example.instagram.repository.UserRepository;
import com.example.instagram.security.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public List<Post> getAll(String status) {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post not found with id:"+id));
    }

    @Override
    public Post savePost(Post post, String authorization) {
        if(post.getId()==0) {
            String email = jwtTokenUtil.getUsernameFromToken(authorization.substring(7));
            User user = userRepository.findByEmailAndEnabled(email,true);
            post.setUser(user);
        }
        return postRepository.save(post);
    }
}
