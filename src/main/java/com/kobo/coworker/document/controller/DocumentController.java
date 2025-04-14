package com.kobo.coworker.document.controller;

import com.kobo.coworker.document.dto.DocumentInfoDto;
import com.kobo.coworker.document.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(
            path = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<DocumentInfoDto> uploadFile(
            Principal principal,
            @RequestPart("file") MultipartFile file){
        DocumentInfoDto documentInfoDto = documentService.uploadDocument(principal, file);
        return ResponseEntity.ok(documentInfoDto);
    }

}
