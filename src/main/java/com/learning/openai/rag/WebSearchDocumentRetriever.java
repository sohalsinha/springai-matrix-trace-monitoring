package com.learning.openai.rag;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

public class WebSearchDocumentRetriever implements DocumentRetriever {

    private static final Logger log = LoggerFactory.getLogger(WebSearchDocumentRetriever.class);

    private static final String TAVILY_API_KEY = "TAVILY_API_KEY";
    private static final String TAVILY_BASE_URL = "https://api.tavily.com/search";
    private static final int DEFAULT_RESULT_LIMIT = 5;
    private final int resultLimit;
    private final RestClient restClient;

    public WebSearchDocumentRetriever(RestClient.Builder clientBuilder , int resultLimit) {
        Assert.notNull(clientBuilder, "clientBuilder must not be null");
        String apiKey = System.getProperty(TAVILY_API_KEY);
        if(apiKey==null){
            apiKey = "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";
        }
        //Assert.hasText(apiKey, "Environment Varaiable " + TAVILY_API_KEY + " must be set");
        this.restClient = clientBuilder
                .baseUrl(TAVILY_BASE_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION , "Bearer "+apiKey)
                .build();

        if(resultLimit <= 0){
            throw new IllegalArgumentException("resultLimit must be greater than 0");
        }
        this.resultLimit = resultLimit;
    }

    @Override
    public List<Document> retrieve(Query query) {
        log.debug("Retrieving documents from Web Search API {} ", query.text());
        Assert.notNull(query, "query must not be null");

        String q=query.text();
        Assert.hasText(q, "query text must not be empty");

        TavilyResponsePayload responsePayload = restClient.post()
                .body(new TavilyRequestPayload(q,"advanced", resultLimit))
                .retrieve()
                .body(TavilyResponsePayload.class);
        if(responsePayload == null || CollectionUtils.isEmpty(responsePayload.hits())){
            return List.of();
        }

        List<Document> documents = new ArrayList<>(responsePayload.hits().size());
        for(TavilyResponsePayload.Hit hit : responsePayload.hits()){
            Document doc = Document.builder().text(hit.content())
                    .metadata("title",hit.title())
                    .metadata("url",hit.url())
                    .score(hit.score())
                    .build();
            documents.add(doc);
        }
        return documents;
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    record TavilyRequestPayload(String query,String searchDepth, int maxResults) {}

    record TavilyResponsePayload(List<Hit> hits) {
        record Hit(String title, String url, String content, Double score) {}
    }

    public static class Builder {
        private RestClient.Builder clientBuilder;
        private int resultLimit = DEFAULT_RESULT_LIMIT;

        private Builder(){}

        public Builder restClientBuilder(RestClient.Builder clientBuilder){
            this.clientBuilder = clientBuilder;
            return this;
        }

        public Builder maxResults(int maxResults){
            if(maxResults <= 0){
                throw new IllegalArgumentException("maxResults must be greater than 0");
            }
            this.resultLimit = maxResults;
            return this;
        }

        public WebSearchDocumentRetriever build(){
           return new WebSearchDocumentRetriever(clientBuilder, resultLimit);
        }
    }

    public static WebSearchDocumentRetriever.Builder builder() {
        return new WebSearchDocumentRetriever.Builder();
    }
}
