package com.ether.ai_chat_server.controller;

import com.ether.ai_chat_server.dto.*;
import com.ether.ai_chat_server.model.Conversation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ether.ai_chat_server.service.ChatService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    ChatController(ChatService chatService){
        this.chatService = chatService;
    }

    @GetMapping("/conversations")
    public List<ConversationResponse> allConversations(
            Principal principal,
            @RequestParam(defaultValue = "descCreatedAt") String sort
    ){
        return chatService.getAllConversations(principal.getName(), sort);
    }

    @GetMapping("/conversation/{id}")
    public List<MessageResponse> conversationHistory(@PathVariable Long id, Principal principal){
        return chatService.buildResponseHistory(id);
    }

    @PostMapping("/conversation/create")
    public ConversationResponse createConversation(@RequestBody ConversationRequest request, Principal principal){
        Conversation conversation = chatService.createConversation(request.title(), principal.getName());
        return new ConversationResponse(
                conversation.getId(),
                conversation.getTitle(),
                conversation.getCreatedAt(),
                conversation.getLastUpdatedAt()
        );
    }

    @PostMapping("/conversation/{id}/sendMessage")
    public ChatResponse sendMessageToConversation(@PathVariable Long id, @RequestBody ChatRequest request, Principal principal){
        String reply = chatService.processMessage(request.message(), id);
        return new ChatResponse(reply, id);
    }

    @DeleteMapping("/conversation/{id}/delete")
    public ResponseEntity<Void> deleteConversation(@PathVariable Long id, Principal principal){
        chatService.deleteConversation(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/conversation/{id}/edit")
    public ResponseEntity<Void> editConversationTitle(@PathVariable Long id, @RequestBody ConversationEditRequest request, Principal principal){
        chatService.editConversationTitle(id, request.newTitle(), principal.getName());
        return ResponseEntity.noContent().build();
    }
}
