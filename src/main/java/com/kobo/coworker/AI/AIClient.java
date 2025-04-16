package com.kobo.coworker.AI;

import com.kobo.coworker.common.apiPayload.code.status.ErrorStatus;
import com.kobo.coworker.common.apiPayload.exception.GeneralException;
import com.kobo.coworker.document.domain.Document;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
public class AIClient {

    private static final Duration TIMEOUT = Duration.ofMinutes(30);
    private final HttpClient client;
    private final String aiServerUrl;

    public AIClient(@Value("${AI.server.url}") String aiServerUrl) {
        this.client = HttpClient.newBuilder()
                .connectTimeout(TIMEOUT)
                .build();
        this.aiServerUrl = aiServerUrl;
    }

    public String analyzeQuestion(Document document, String content) {
        String requestBody = buildRequestBody(document, content);
        URI uri = createUri();
        HttpRequest request = buildHttpRequest(uri, requestBody);
        HttpResponse<String> response = sendSafeRequest(request);

        return handleResponse(response);
    }

    private String buildRequestBody(Document document, String content) {
        JSONObject json = new JSONObject();
        if (ensureDocumentIsPresent(document)) {
            json.put("originalFilename", document.getOriginalFilename());
            json.put("fileUrl", document.getFileUrl());
        }
        json.put("content", content);
        return json.toString();
    }

    private boolean ensureDocumentIsPresent(Document document) {
        return document.getOriginalFilename() != null || document.getFileUrl() != null;
    }

    private URI createUri() {
        try {
            return new URI(aiServerUrl);
        } catch (URISyntaxException e) {
            throw new GeneralException(ErrorStatus.AI_SERVER_INVALID_URI);
        }
    }

    private HttpRequest buildHttpRequest(URI uri, String body) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .timeout(TIMEOUT)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }

    private HttpResponse<String> sendSafeRequest(HttpRequest request) {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new GeneralException(ErrorStatus.AI_SERVER_IO_ERROR);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GeneralException(ErrorStatus.AI_SERVER_REQUEST_INTERRUPTED);
        }
    }

    private String handleResponse(HttpResponse<String> response) {
        if (response.statusCode() != 200) {
            throw new GeneralException(ErrorStatus.AI_SERVER_COMMUNICATION_ERROR);
        }
        return response.body();
    }

}
