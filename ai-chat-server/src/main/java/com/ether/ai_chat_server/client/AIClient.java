package com.ether.ai_chat_server.client;

import com.ether.ai_chat_server.dto.ChatChunk;

import java.util.List;

public interface AIClient {

    public String ask(List<ChatChunk> conversation);
}
