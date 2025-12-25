package com.learning.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping("/api")
public class ChatMemoryController {

    private ChatClient chatClient;

    private ChatMemoryController(@Qualifier("chatMemoryChatClient")
                                 ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/chat-memory")
    public String chatMemory(@RequestParam("username") String username,
                             @RequestParam("message") String message) {
    //advisorSpec - param CONVERSATION_ID makes the id as unique to each user.
        return chatClient
                .prompt()
                .user(message).advisors(
                        advisorSpec -> advisorSpec.param(CONVERSATION_ID, username)
                )
                .call()
                .content();

       //Testing at DB at - http://localhost:8080/h2-console/login.do
        /**
         *
          */
    }

}
