package com.learning.openai.config;

import com.learning.openai.advisors.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatClientConfig {
    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {

        ChatOptions options = ChatOptions.builder()
                .model("gpt-4o-mini")
                .temperature(0.3)
                .maxTokens(200)
                .presencePenalty(0.6)
                .stopSequences(List.of("END"))
                .build();

        return chatClientBuilder
                .defaultOptions(options)
                .defaultSystem("""
                                You are an internal HR assistant, Your Role is to help\s
                                employees with questions related to HR Policies, Such as leave policies, working hours\s
                                benifits and code of conduct. if a user ask for the help with anything outside of these\s 
                                topics Kindly inform them that you can only assist with Queries related to HR Policies\s
                                Leaves in total allocated to each employee\s 
                                are as mentioned below:
                                Casual = 5
                                Earned = 10
                                Total 15 annually.                                
                                """)
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(),new TokenUsageAuditAdvisor()))
                .build();

    }
}
