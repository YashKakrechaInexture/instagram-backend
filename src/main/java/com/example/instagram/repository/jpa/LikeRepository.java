package com.example.instagram.repository.jpa;

import com.example.instagram.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {
    boolean existsByPost_IdAndUser_Id(Long postId, Long userId);
    Optional<Like> getByPost_IdAndUser_Id(Long postId, Long userId);
    long countAllByPost_Id(long postId);
}
