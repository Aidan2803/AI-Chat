package com.ether.ai_chat_server.dto;

import com.ether.ai_chat_server.model.enums.MessageType;

import java.util.List;

public record ChatChunk(MessageType role, String content) {
}
