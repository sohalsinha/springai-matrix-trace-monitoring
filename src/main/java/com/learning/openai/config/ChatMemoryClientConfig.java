package com.learning.openai.config;

import com.learning.openai.advisors.TokenUsageAuditAdvisor;
import com.learning.openai.rag.PIIMaskingDocumentPostProcessor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatMemoryClientConfig {

    @Bean
    ChatMemory chatMemory(JdbcChatMemoryRepository repository) {
        //Using MAX Message to Limit Chat History for the user.
        return  MessageWindowChatMemory.builder().maxMessages(100)
                .chatMemoryRepository(repository).build();
    }

    /*
    This is commented to have addition of RetrievalAugmentationAdvisor
    @Bean("chatMemoryChatClient")
    public ChatClient ChatMemoryClientConfig(ChatClient.Builder chatClientBuilder
            , ChatMemory chatMemory) {
        Advisor loggerAdvisor = new SimpleLoggerAdvisor();
        Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();

        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return chatClientBuilder
                .defaultAdvisors(List.of(loggerAdvisor, memoryAdvisor,tokenUsageAdvisor))
                .build();

    }
    */
    @Bean("chatMemoryChatClient")
    public ChatClient ChatMemoryClientConfig(ChatClient.Builder chatClientBuilder
            , ChatMemory chatMemory
            ,RetrievalAugmentationAdvisor retrievalAugmentationAdvisor) {
        Advisor loggerAdvisor = new SimpleLoggerAdvisor();
        Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();

        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return chatClientBuilder
                .defaultAdvisors(List.of(loggerAdvisor, memoryAdvisor,tokenUsageAdvisor,retrievalAugmentationAdvisor))
                .build();

    }

    //Changes for the RAG - with Query Transformer
    @Bean
    RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore,
            ChatClient.Builder chatClientBuilder) {
     return  RetrievalAugmentationAdvisor.builder()
             .queryTransformers(TranslationQueryTransformer.builder()
                     .chatClientBuilder(chatClientBuilder.clone())
                     .targetLanguage("englist").build())
             .documentRetriever(
                VectorStoreDocumentRetriever.builder().vectorStore(vectorStore)
                        .topK(3)
                        .similarityThreshold(0.5).build())
             .documentPostProcessors(PIIMaskingDocumentPostProcessor.builder())
             .build();
    }
}
