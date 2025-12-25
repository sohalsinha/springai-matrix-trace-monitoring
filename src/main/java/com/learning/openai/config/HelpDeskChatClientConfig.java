package com.learning.openai.config;

import com.learning.openai.advisors.TokenUsageAuditAdvisor;
import com.learning.openai.tools.TimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

@Configuration
public class HelpDeskChatClientConfig {

    @Value("classpath:/templates/HelpDeskSystemPromptTemplate.st")
    Resource helpDeskSystemPromptTemplate;

    @Bean("helpDeskChatClient")
    public ChatClient ChatMemoryClientConfig(ChatClient.Builder chatClientBuilder
            , ChatMemory chatMemory, TimeTools timeTools) {
        Advisor loggerAdvisor = new SimpleLoggerAdvisor();
        Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();
        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        return chatClientBuilder
                .defaultSystem(helpDeskSystemPromptTemplate)
                .defaultTools(timeTools) //TOOLS Injection here
                .defaultAdvisors(List.of(loggerAdvisor, memoryAdvisor,tokenUsageAdvisor                        ))
                .build();

    }

}
