package com.kobo.coworker.AI.controller;

import com.kobo.coworker.AI.service.OpenAiService;
import com.kobo.coworker.program.domain.SupportProgram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OpenAiController {

    private final OpenAiService openAiService;

    @Autowired
    public OpenAiController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @GetMapping("/programs")
    public ResponseEntity<List<SupportProgram>> getPrograms() {
        String input = "Provide data for supportPrograms.";
        List<SupportProgram> supportPrograms = openAiService.callOpenAiApi(input);
        return ResponseEntity.ok(supportPrograms);
    }
}
