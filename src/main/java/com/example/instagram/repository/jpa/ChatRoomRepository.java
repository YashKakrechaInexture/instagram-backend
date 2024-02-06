package com.example.instagram.repository.jpa;

import com.example.instagram.model.ChatRoom;
import com.example.instagram.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,String> {

    ChatRoom findBySenderAndRecipient(User sender, User recipient);
    List<ChatRoom> findAllByRecipient(User recipient);
}
