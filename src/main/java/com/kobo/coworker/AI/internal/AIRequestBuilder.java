package com.kobo.coworker.AI.internal;

import com.kobo.coworker.common.apiPayload.code.status.ErrorStatus;
import com.kobo.coworker.common.apiPayload.exception.GeneralException;
import com.kobo.coworker.document.domain.Document;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class AIRequestBuilder {

    private static final Duration TIMEOUT = Duration.ofMinutes(30);
    private final HttpClient client;
    private final String aiServerUrl;

    public AIRequestBuilder(String aiServerUrl) {
        this.client = HttpClient.newBuilder().connectTimeout(TIMEOUT).build();
        this.aiServerUrl = aiServerUrl;
    }

    public HttpRequest build(String content) {
        JSONObject json = new JSONObject();
        json.put("content", content);
        return buildRequest(json.toString());
    }

    public HttpRequest build(Document document, String content) {
        JSONObject json = new JSONObject();
        json.put("originalFilename", document.getOriginalFilename());
        json.put("fileUrl", document.getFileUrl());
        json.put("content", content);
        return buildRequest(json.toString());
    }

    private HttpRequest buildRequest(String body) {
        try {
            URI uri = new URI(aiServerUrl);
            return HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .timeout(TIMEOUT)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
        } catch (URISyntaxException e) {
            throw new GeneralException(ErrorStatus.AI_SERVER_INVALID_URI);
        }
    }

    public HttpResponse<String> send(HttpRequest request) {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new GeneralException(ErrorStatus.AI_SERVER_IO_ERROR);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GeneralException(ErrorStatus.AI_SERVER_REQUEST_INTERRUPTED);
        }
    }
}
