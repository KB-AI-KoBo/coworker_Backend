package com.kobo.coworker.AI;

import com.kobo.coworker.common.apiPayload.code.status.ErrorStatus;
import com.kobo.coworker.common.apiPayload.exception.GeneralException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

    public String analyzeQuestion(String originalFilename, String fileUrl, String content) {
        String requestBody = buildRequestBody(originalFilename, fileUrl, content);
        URI uri = createUri(AI_SERVER_URL);
        HttpRequest request = buildHttpRequest(uri, requestBody);
        HttpResponse<String> response = sendSafeRequest(request);

        return handleResponse(response);
    }

    private String buildRequestBody(String originalFilename, String fileUrl, String content) {
        JSONObject json = new JSONObject();
        if (originalFilename != null && !originalFilename.isBlank()) {
            json.put("originalFilename", originalFilename);
        }
        if (fileUrl != null && !fileUrl.isBlank()) {
            json.put("fileUrl", fileUrl);
        }
        json.put("content", content);
        return json.toString();
    }

    private URI createUri(String url) {
        try {
            return new URI(url);
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
