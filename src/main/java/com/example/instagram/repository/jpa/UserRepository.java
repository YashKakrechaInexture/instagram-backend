package com.example.instagram.repository.jpa;

import com.example.instagram.dto.projections.MessagePageProfileProjection;
import com.example.instagram.dto.projections.SearchUserProjection;
import com.example.instagram.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    User findByUsername(String username);
    User findByEmailAndEnabled(String email, boolean enabled);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    List<SearchUserProjection> findByUsernameStartingWith(String username);
    List<SearchUserProjection> findByUsernameStartsWithAndUsernameNot(String username, String selfUsername);
    List<SearchUserProjection> findByUsernameIsStartingWith(String username);
    MessagePageProfileProjection findByUsernameIs(String username);
}
