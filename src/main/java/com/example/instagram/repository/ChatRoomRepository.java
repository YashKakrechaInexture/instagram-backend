package com.example.instagram.repository;

import com.example.instagram.model.ChatRoom;
import com.example.instagram.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,String> {

    ChatRoom findBySenderAndAndRecipient(User sender, User recipient);
}
