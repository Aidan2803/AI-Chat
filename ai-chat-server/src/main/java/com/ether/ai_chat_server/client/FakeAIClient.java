package com.ether.ai_chat_server.client;

import com.ether.ai_chat_server.dto.ChatChunk;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@ConditionalOnProperty(name = "ai.provider", havingValue = "fake", matchIfMissing = true)
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
    public String ask(List<ChatChunk> conversation){
        return "What a great question `" + conversation.get(conversation.size() - 1) + "` "+ randomResponsesList.get(rand.nextInt(4));
    }
}
