package com.kobo.coworker.analysis.repository;

import com.kobo.coworker.analysis.domain.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, Long> {

    List<AnalysisResult> findByDocumentId(Long documentId);
    List<AnalysisResult> findByQuestionId(Long questionId);
}
