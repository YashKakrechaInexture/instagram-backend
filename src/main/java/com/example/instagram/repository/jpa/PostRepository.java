package com.example.instagram.repository.jpa;

import com.example.instagram.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    long countByUser_Id(long id);

    List<Post> findAllByUser_IdOrderByCreateDateTimeDesc(long id);
}
