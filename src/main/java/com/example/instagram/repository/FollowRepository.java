package com.example.instagram.repository;

import com.example.instagram.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {
    long countByFollowerUser_Id(long id);
    long countByFollowingToUser_Id(long id);
    boolean existsByFollowerUser_IdAndFollowingToUser_Id(long followerUser, long followingToUser);
    Follow findByFollowerUser_IdAndFollowingToUser_Id(long followerUser, long followingToUser);
    List<Follow> findAllByFollowerUser_Id(long followerUser);
    List<Follow> findAllByFollowingToUser_Id(long followingToUser);
}
