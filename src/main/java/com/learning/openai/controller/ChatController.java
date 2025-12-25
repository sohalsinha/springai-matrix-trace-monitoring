package com.learning.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {

    private ChatClient chatClient;

    private ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {

        return chatClient
                .prompt()
                //.advisors(new TokenUsageAuditAdvisor())
                //Override the message of default system as shown below
                /*.system("""
                                You are an internal IT assistant, Your Role is to help\s
                                employees with questions related to IT Policies, Such as IT policies, working hours\s
                                benifits and code of conduct. if a user ask for the help with anything outside of these\s 
                                topics Kindly inform them that you can only assist with Queries related to IT Policies\s
                                Such as password reset
                                """)*/
                .user(message)
                .call()
                .content();
    }

}
