package com.ether.ai_chat_server.controller;

import com.ether.ai_chat_server.dto.*;
import com.ether.ai_chat_server.model.Conversation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ether.ai_chat_server.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    ChatController(ChatService chatService){
        this.chatService = chatService;
    }

    @GetMapping("/conversations")
    public List<ConversationResponse> allConversations(){ return chatService.getAllConversations();}

    @GetMapping("/conversation/{id}")
    public List<MessageResponse> conversationHistory(@PathVariable Long id){
        return chatService.buildResponseHistory(id);
    }

    @PostMapping("/conversation/create")
    public ConversationResponse createConversation(@RequestBody ConversationRequest request){
        Conversation conversation = chatService.createConversation(request.title());
        return new ConversationResponse(conversation.getId(), conversation.getTitle());
    }

    @PostMapping("/conversation/{id}/sendMessage")
    public ChatResponse sendMessageToConversation(@PathVariable Long id, @RequestBody ChatRequest request){
        String reply = chatService.processMessage(request.message(), id);
        return new ChatResponse(reply, id);
    }

    @DeleteMapping("/conversation/{id}/delete")
    public ResponseEntity<Void> deleteConversation(@PathVariable Long id){
        chatService.deleteConversation(id);
        return ResponseEntity.noContent().build();
    }
}
