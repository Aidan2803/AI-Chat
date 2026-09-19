package com.ether.ai_chat_server.repository;

import com.ether.ai_chat_server.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
