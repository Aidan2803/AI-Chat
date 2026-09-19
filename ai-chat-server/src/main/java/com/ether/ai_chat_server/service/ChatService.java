package com.ether.ai_chat_server.service;

import com.ether.ai_chat_server.client.AIClient;
import com.ether.ai_chat_server.dto.ConversationResponse;
import com.ether.ai_chat_server.dto.MessageResponse;
import com.ether.ai_chat_server.exceptions.ConversationNotFoundException;
import com.ether.ai_chat_server.model.Conversation;
import com.ether.ai_chat_server.model.Message;
import com.ether.ai_chat_server.model.enums.MessageType;
import com.ether.ai_chat_server.repository.ConversationRepository;
import com.ether.ai_chat_server.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatService {
    private final AIClient aiClient;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    private void createAndSaveMessage(String text, Conversation conversation, MessageType messageType){
        Message messageObj = new Message();
        messageObj.setText(text);
        messageObj.setConversation(conversation);
        messageObj.setMessageType(messageType);
        messageRepository.save(messageObj);
    }

    public ChatService(AIClient aiClient, ConversationRepository conversationRepository, MessageRepository messageRepository){
        this.aiClient = aiClient;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    public List<MessageResponse> buildResponseHistory(Long conversationID){
        conversationRepository.findById(conversationID).orElseThrow(() -> new ConversationNotFoundException("Could not find conversation by id " + conversationID));

        return messageRepository.findByConversationIdOrderByIdAsc(conversationID)
                .stream()
                .map(m -> new MessageResponse(m.getId(), m.getText())).toList();
    }

    @Transactional
    public String processMessage(String message, Long conversationID){
        System.out.println("processMessage with messageObj: " + message + "for conversationID: " + conversationID);

        Conversation conversation = conversationRepository.findById(conversationID)
                    .orElseThrow(() -> new ConversationNotFoundException("Conversation not found"));

        String serverResponse = aiClient.ask(message);

        createAndSaveMessage(message, conversation, MessageType.user);
        createAndSaveMessage(serverResponse, conversation, MessageType.ai);

        return serverResponse;
    }

    public Conversation createConversation(String title){
        Conversation conversation = new Conversation();
        conversation.setTitle(title);

        return conversationRepository.save(conversation);
    }

    public List<ConversationResponse> getAllConversations(){
        return conversationRepository.findAll().stream()
                .map(c -> new ConversationResponse(c.getId(), c.getTitle()))
                .toList();

    }

    public void deleteConversation(Long id){
        if (!conversationRepository.existsById(id)){
            throw new ConversationNotFoundException("Conversation " + id + "not found");
        }
        conversationRepository.deleteById(id);
    }
}
