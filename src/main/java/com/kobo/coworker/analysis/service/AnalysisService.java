package com.kobo.coworker.analysis.service;

import com.kobo.coworker.analysis.domain.AnalysisResult;
import com.kobo.coworker.analysis.dto.AnalysisResultReqDto;
import com.kobo.coworker.analysis.repository.AnalysisResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AnalysisService {

    private final AnalysisResultRepository analysisResultRepository;

    @Autowired
    public AnalysisService(AnalysisResultRepository analysisResultRepository) {
        this.analysisResultRepository = analysisResultRepository;
    }

    @Transactional
    public void saveAnalysisResult(AnalysisResultReqDto analysisResultReqDto) {
        AnalysisResult result = analysisResultReqDto.toEntity();
        analysisResultRepository.save(result);
    }

    public Optional<AnalysisResult> findAnalysisResultById(Long id) {
        return analysisResultRepository.findById(id);
    }

    public List<AnalysisResult> findAnalysisResultsByDocumentId(Long documentId) {
        return analysisResultRepository.findByDocument_DocumentId(documentId);
    }

    public void deleteAnalysisResultById(Long id) {
        analysisResultRepository.deleteById(id);
    }
}
