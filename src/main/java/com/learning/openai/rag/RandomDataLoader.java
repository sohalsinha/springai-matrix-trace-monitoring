package com.learning.openai.rag;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

//@Component
public class RandomDataLoader {

    private final VectorStore vectorStore;

    public RandomDataLoader(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void loadSentencesIntoVectorStore() {
        List<String> sentences = List.of(
          "Java is used for building Scable enterprices applications",
                "Python is commoinly used for machine learning and automation tasks",
                "JavaScript is essential for the creation of interactive web pages.",
                "The stock market opens at 9 am.",
                "Walking is good for health"

        );
        List<Document> documents = sentences.stream().map(Document::new).collect(Collectors.toList());
        vectorStore.add(documents);
    }
}
