package com.ether.ai_chat_server.dto;

import java.time.Instant;

public record ConversationResponse(Long conversationID, String title, Instant createdAt, Instant lastUpdatedAt) {
}
