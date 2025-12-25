package com.learning.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class StreamController {


    private ChatClient chatClient;

    private StreamController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    //Stream compatibility is with Broweser to return response as when received
    // Test via browser - http://localhost:8080/api/stream?message=Tell%20me%20about%20HR%20Policies


    @GetMapping("/stream")
    public Flux<String> stream(@RequestParam("message") String message) {

        return chatClient
                .prompt()
                .user(message)
                .stream()
                .content();
    }


}
