package com.kobo.coworker.document.repository;

import com.kobo.coworker.document.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByDocumentId(Long documentId);
    Optional<Document> findByFileUrl(String fileUrl);
}
