package com.kobo.coworker.document.controller;

import com.kobo.coworker.document.dto.UploadResDto;
import com.kobo.coworker.document.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.Principal;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<UploadResDto> uploadFile(
            Principal principal,
            @RequestPart("file") MultipartFile file) throws IOException {
        UploadResDto uploadResDto = documentService.uploadDocument(principal, file);
        return ResponseEntity.ok(uploadResDto);
    }
}
