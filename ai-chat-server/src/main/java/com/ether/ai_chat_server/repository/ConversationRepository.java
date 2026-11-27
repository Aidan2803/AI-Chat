package com.ether.ai_chat_server.repository;

import com.ether.ai_chat_server.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByUserId(Long userId);
    List<Conversation> findByUserUsername(String username);
    List<Conversation> findByUserUsernameOrderByCreatedAtAsc(String username);
    List<Conversation> findByUserUsernameOrderByCreatedAtDesc(String username);
}
