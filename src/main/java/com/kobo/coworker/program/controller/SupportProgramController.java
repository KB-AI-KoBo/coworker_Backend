package com.kobo.coworker.program.controller;

import com.kobo.coworker.program.domain.SupportProgram;
import com.kobo.coworker.program.service.SupportProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/support-programs")
public class SupportProgramController {

    private final SupportProgramService supportProgramService;

    @Autowired
    public SupportProgramController(SupportProgramService supportProgramService) {
        this.supportProgramService = supportProgramService;
    }

    @GetMapping
    public ResponseEntity<List<SupportProgram>> getPrograms(
            @RequestParam(required = false) String field,
            @RequestParam(required = false) String supervisingAgency,
            @RequestParam(required = false) String applicationStartDate,
            @RequestParam(required = false) String applicationEndDate) {
        List<SupportProgram> supportPrograms = supportProgramService.getFilteredPrograms(field, supervisingAgency, applicationStartDate, applicationEndDate);
        return ResponseEntity.ok(supportPrograms);
    }

}
