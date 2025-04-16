package com.kobo.coworker.AI;

import com.kobo.coworker.AI.internal.AIRequestBuilder;
import com.kobo.coworker.AI.internal.AIResponseHandler;
import com.kobo.coworker.document.domain.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AIClient {

    private final AIRequestBuilder requestBuilder;
    private final AIResponseHandler responseHandler;

    public AIClient(@Value("${AI.server.url}") String aiServerUrl) {
        this.requestBuilder = new AIRequestBuilder(aiServerUrl);
        this.responseHandler = new AIResponseHandler();
    }

    public String analyzeQuestion(String content) {
        var request = requestBuilder.build(content);
        var response = requestBuilder.send(request);
        return responseHandler.handle(response);
    }

    public String analyzeQuestionAndDocument(Document document, String content) {
        var request = requestBuilder.build(document, content);
        var response = requestBuilder.send(request);
        return responseHandler.handle(response);
    }
}
