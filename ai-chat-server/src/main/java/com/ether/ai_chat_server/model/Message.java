package com.ether.ai_chat_server.model;

import com.ether.ai_chat_server.model.enums.MessageType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String text;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    public void setText(String text){this.text = text;}
    public void setConversation(Conversation conversation){this.conversation = conversation;}
    public void setMessageType(MessageType messageType){this.messageType = messageType;}

    public Long getId(){return id;}
    public String getText(){return text;}
    public MessageType getMessageType(){return  messageType;}
}
