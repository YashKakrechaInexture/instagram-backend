package com.example.instagram.service;

import com.example.instagram.model.Like;
import com.example.instagram.model.Post;
import com.example.instagram.model.User;
import com.example.instagram.repository.LikeRepository;
import com.example.instagram.security.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService{

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public ResponseEntity<?> likePost(Post post, String authorization) {
        long userId = Long.parseLong(jwtTokenUtil.getClaimFromToken(authorization.substring(7),
                claims -> claims.get(JwtTokenUtil.JWT_ID)).toString());
        boolean exist = likeRepository.existsByPost_IdAndUser_Id(post.getId(), userId);
        if(exist){
            throw new IllegalArgumentException("Like already exists!");
        }
        User user = new User();
        user.setId(userId);
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);
        return ResponseEntity.ok("Liked post successfully!");
    }

    @Override
    public ResponseEntity<?> unlikePost(Post post, String authorization) {
        long userId = Long.parseLong(jwtTokenUtil.getClaimFromToken(authorization.substring(7),
                claims -> claims.get(JwtTokenUtil.JWT_ID)).toString());
        Like like = likeRepository.getByPost_IdAndUser_Id(post.getId(), userId).orElseThrow(() -> new IllegalArgumentException("Like does not exists!"));
        likeRepository.delete(like);
        return ResponseEntity.ok("Unliked post successfully!");
    }
}
