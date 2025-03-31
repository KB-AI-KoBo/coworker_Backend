package com.kobo.coworker.AI;

import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class AIClient {

    public String analyzeQuestion(Long documentId, String content) throws Exception {
        JSONObject json = new JSONObject();
        json.put("documentId", documentId);
        json.put("content", content);

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMinutes(30))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://coworkerkobo.shop/"))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofMinutes(30)) // 응답 대기 30분 타임아웃
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new RuntimeException("AI 서버 호출 실패: " + response.statusCode());
        }
    }
}
