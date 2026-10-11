package com.ether.ai_chat_server.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.awt.*;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "ai.provider", havingValue = "ollama")
public class Ollamaclient implements  AIClient{

    private final RestClient restClient;
    private final String model;

    public Ollamaclient(
            @Value("${ai.ollama.base-url:http://localhost:11434}") String baseUrl,
            @Value("${ai.ollama.model:llama3.2:3b)}") String model
    ){
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(Duration.ofSeconds(5));
        simpleClientHttpRequestFactory.setReadTimeout(Duration.ofMinutes(3));

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(simpleClientHttpRequestFactory)
                .build();

        this.model = model;
    }

    @Override
    public String ask(String message){
        Map<String, Object> body = Map.of(
                "model", model,
                "stream", false,
                "messages", List.of(Map.of("role", "user", "content", message))
        );

        try{
            Map<?, ?> response = restClient.post()
                    .uri("/api/chat")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            if (response != null &&
                    response.get("message") instanceof Map<?, ?> msg
                    && msg.get("content") instanceof String content){
                return content;
            }

            throw new IllegalStateException("Unexpected response from Ollama: " + response);
        }
        catch (RestClientException e){
            throw new IllegalStateException("Could not reach Ollama");
        }
    }
}
