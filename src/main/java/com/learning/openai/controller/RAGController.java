package com.learning.openai.controller;

import com.learning.openai.advisors.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping("/api/rag")
public class RAGController {

    private final ChatClient chatClient;
    private final ChatClient webSearchChatClient;
    private final VectorStore vectorStore;

    private RAGController(@Qualifier("chatMemoryChatClient") ChatClient chatClient,
            @Qualifier("webSearchRAGChatClient") ChatClient webSearchChatClient
            , VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
        this.webSearchChatClient = webSearchChatClient;
    }


    @Value("classpath:/templates/systemPromptRandomDataTemplate.st")
    Resource systemPromptRandomDataTemplate;

    @Value("classpath:/templates/systemPromptRandomDataTemplatePDF.st")
    Resource hrSystemTemplate;

    /*@GetMapping("/random/chat")
    public ResponseEntity<String> randomChat(@RequestHeader("username") String username,
                                               @RequestParam("message") String message) {

        //Recommended Topk - 3 to get more accurate answer
        SearchRequest request = SearchRequest.builder().query(message)
                .topK(3)
                .similarityThreshold(0.5)
                .build();

        List<Document> similarDocs = vectorStore.similaritySearch(request);

        String similarcontext = similarDocs.stream().map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));

        String answer = chatClient.prompt().system(
                promptSystemSpec -> promptSystemSpec.text(systemPromptRandomDataTemplate).param("documents",similarcontext))
                .advisors(a -> a.param(CONVERSATION_ID, username))
                .user(message)
                .call().content();

        return  ResponseEntity.ok(answer);

    }


    @GetMapping("/document/chat")
    public ResponseEntity<String> documentChat(@RequestHeader("username") String username,
                                             @RequestParam("message") String message) {


        SearchRequest request = SearchRequest.builder().query(message)
                .topK(3)
                .similarityThreshold(0.5)
                .build();
        List<Document> similarDocs = vectorStore.similaritySearch(request);
        String similarcontext = similarDocs.stream().map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));

        String answer = chatClient.prompt().system(
                        promptSystemSpec -> promptSystemSpec.text(hrSystemTemplate).param("documents",similarcontext))
                .advisors(a -> a.param(CONVERSATION_ID, username))
                .user(message)
                .call().content();

        return  ResponseEntity.ok(answer);

    }*/

    //With RAG Changes in Config
    @GetMapping("/random/chat")
    public ResponseEntity<String> randomChat(@RequestHeader("username") String username,
                                             @RequestParam("message") String message) {

        //Recommended Topk - 3 to get more accurate answer
       /* SearchRequest request = SearchRequest.builder().query(message)
                .topK(3)
                .similarityThreshold(0.5)
                .build();

        List<Document> similarDocs = vectorStore.similaritySearch(request);

        String similarcontext = similarDocs.stream().map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));*/

        String answer = chatClient.prompt()
                /*.system(
                        promptSystemSpec -> promptSystemSpec.text(systemPromptRandomDataTemplate).param("documents",similarcontext))
                */
                .advisors(a -> a.param(CONVERSATION_ID, username))
                .user(message)
                .call().content();

        return  ResponseEntity.ok(answer);

    }

    //With RAG Changes in Config
    @GetMapping("/document/chat")
    public ResponseEntity<String> documentChat(@RequestHeader("username") String username,
                                               @RequestParam("message") String message) {


       /* SearchRequest request = SearchRequest.builder().query(message)
                .topK(3)
                .similarityThreshold(0.5)
                .build();
        List<Document> similarDocs = vectorStore.similaritySearch(request);
        String similarcontext = similarDocs.stream().map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));*/

        String answer = chatClient.prompt()
                /*.system(
                        promptSystemSpec -> promptSystemSpec.text(hrSystemTemplate).param("documents",similarcontext))
                */.advisors(a -> a.param(CONVERSATION_ID, username))
                .user(message)
                .call().content();

        return  ResponseEntity.ok(answer);

    }

    //With RAG Changes in Config
    @GetMapping("/web-search/chat")
    public ResponseEntity<String> webSearchChat(@RequestHeader("username") String username,
                                               @RequestParam("message") String message) {

        String answer = webSearchChatClient.prompt()
                .advisors(a -> a.param(CONVERSATION_ID, username))
                .user(message)
                .call().content();

        return  ResponseEntity.ok(answer);

    }

}
