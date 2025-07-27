package com.kobo.coworker.common;

import com.kobo.coworker.common.apiPayload.code.status.ErrorStatus;
import com.kobo.coworker.common.apiPayload.exception.GeneralException;
import com.kobo.coworker.document.domain.Document;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(AIClient.class);
    private static final Duration TIMEOUT = Duration.ofMinutes(30);
    private final HttpClient client;
    private final String aiServerUrl;

    public AIClient(@Value("${AI.server.url}") String aiServerUrl) {
        this.client = HttpClient.newBuilder()
                .connectTimeout(TIMEOUT)
                .build();
        this.aiServerUrl = aiServerUrl;
    }

    public String analyzeQuestion(Document document, String content, String jwtToken) {
        String requestBody = buildRequestBody(document, content);
        URI uri = createUri();
        HttpRequest request = buildHttpRequest(uri, requestBody, jwtToken);
        HttpResponse<String> response = sendSafeRequest(request, requestBody);

        return handleResponse(response);
    }

    private String buildRequestBody(Document file, String content) {
        JSONObject json = new JSONObject();
        if (ensureDocumentIsPresent(file)) {
            JSONObject document = new JSONObject();
            document.put("originalFileName", file.getOriginalFilename());
            document.put("fileUrl", file.getFileUrl());
            json.put("document", document);
        }
        json.put("content", content);
        return json.toString();
    }

    private boolean ensureDocumentIsPresent(Document document) {
        return document != null && (
                document.getOriginalFilename() != null || document.getFileUrl() != null
        );
    }

    private URI createUri() {
        try {
            return new URI(aiServerUrl);
        } catch (URISyntaxException e) {
            log.error("AI 서버 URI가 잘못되었습니다: {}", aiServerUrl, e);
            throw new GeneralException(ErrorStatus.AI_SERVER_INVALID_URI);
        }
    }

    private HttpRequest buildHttpRequest(URI uri, String body, String jwtToken) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .timeout(TIMEOUT)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }

    private HttpResponse<String> sendSafeRequest(HttpRequest request, String requestBody) {
        try {
            log.info("AI 서버 요청 전송 중 - URL: {}, Body: {}", request.uri(), requestBody);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("AI 서버 응답 수신 - statusCode: {}", response.statusCode());
            return response;
        } catch (IOException e) {
            log.error("IOException 발생 - 타입: {}, 메시지: {}", e.getClass().getName(), e.getMessage(), e);
            throw new GeneralException(ErrorStatus.AI_SERVER_IO_ERROR);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("요청이 인터럽트되었습니다: {}", e.getMessage(), e);
            throw new GeneralException(ErrorStatus.AI_SERVER_REQUEST_INTERRUPTED);
        } catch (Exception e) {
            log.error("예상치 못한 예외 발생 - 타입: {}, 메시지: {}", e.getClass().getName(), e.getMessage(), e);
            throw new GeneralException(ErrorStatus.AI_SERVER_COMMUNICATION_ERROR);
        }
    }

    private String handleResponse(HttpResponse<String> response) {
        if (response.statusCode() != 200) {
            log.error("AI 서버 응답 오류 - 상태 코드: {}, 응답 본문: {}", response.statusCode(), response.body());
            throw new GeneralException(ErrorStatus.AI_SERVER_COMMUNICATION_ERROR);
        }
        return response.body();
    }
}
