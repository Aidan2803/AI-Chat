package com.ether.ai_chat_server.dto;

import com.ether.ai_chat_server.model.enums.MessageType;

public record MessageResponse(Long id, String text, MessageType type) {
}
