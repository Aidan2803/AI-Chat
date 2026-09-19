package com.ether.ai_chat_server.client;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Random;

@Component
public class FakeAIClient implements AIClient {
    private final ArrayList<String> randomResponsesList = new ArrayList<>();;
    private final Random rand = new Random();
    public FakeAIClient(){
        randomResponsesList.add("response #1");
        randomResponsesList.add("some ai stuff");
        randomResponsesList.add("ai slop data");
        randomResponsesList.add("real useful info");
    }

    @Override
    public String ask(String message){
        return "What a great question `" + message + "` "+ randomResponsesList.get(rand.nextInt(4));
    }
}
