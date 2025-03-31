package com.kobo.coworker.AI;

import com.kobo.coworker.common.apiPayload.code.status.ErrorStatus;
import com.kobo.coworker.common.apiPayload.exception.GeneralException;
import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class AIClient {

    private static final String AI_SERVER_URL = "http://coworkerkobo.shop/";
    private static final Duration TIMEOUT = Duration.ofMinutes(30);
    private final HttpClient client;

    public AIClient() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(TIMEOUT)
                .build();
    }

    public String analyzeQuestion(Long documentId, String content) throws Exception {
        String requestBody = buildRequestBody(documentId, content);
        HttpRequest request = buildHttpRequest(requestBody);
        HttpResponse<String> response = sendRequest(request);

        return handleResponse(response);
    }

    private String buildRequestBody(Long documentId, String content) {
        JSONObject json = new JSONObject();
        json.put("documentId", documentId);
        json.put("content", content);
        return json.toString();
    }

    private HttpRequest buildHttpRequest(String body) throws Exception {
        return HttpRequest.newBuilder()
                .uri(new URI(AI_SERVER_URL))
                .header("Content-Type", "application/json")
                .timeout(TIMEOUT)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws Exception {
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String handleResponse(HttpResponse<String> response) {
        if (response.statusCode() != 200) {
            throw new GeneralException(ErrorStatus.AI_SERVER_REQUEST_FAILED);
        }
        return response.body();
    }

}
