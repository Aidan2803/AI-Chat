package com.ether.ai_chat_server.exceptions;

public class ConversationNotFoundException extends RuntimeException{
    public ConversationNotFoundException(String message){
        super(message);
    }
}
