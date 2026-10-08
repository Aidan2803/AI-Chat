package com.ether.ai_chat_server.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ApplicationUser user;

    public void setTitle(String title){this.title = title;}
    public String getTitle(){return title;}
    public Long getId(){return id;}
    public List<Message> getMessages(){ return messages; }
    public void setUser(ApplicationUser user) {this.user = user; }
    public ApplicationUser getUser(){return user;}
}
