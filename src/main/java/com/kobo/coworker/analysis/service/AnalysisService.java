package com.kobo.coworker.analysis.service;

import com.kobo.coworker.analysis.domain.AnalysisResult;
import com.kobo.coworker.analysis.dto.AnalysisResultInfoDto;
import com.kobo.coworker.analysis.repository.AnalysisResultRepository;
import com.kobo.coworker.common.apiPayload.code.status.ErrorStatus;
import com.kobo.coworker.common.apiPayload.exception.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AnalysisService {

    private final AnalysisResultRepository analysisResultRepository;

    @Autowired
    public AnalysisService(AnalysisResultRepository analysisResultRepository) {
        this.analysisResultRepository = analysisResultRepository;
    }

    @Transactional
    public void save(AnalysisResultInfoDto analysisResultInfoDto) {
        AnalysisResult result = analysisResultInfoDto.toEntity();
        analysisResultRepository.save(result);
    }

    public AnalysisResultInfoDto getDtoById(Long id) {
        AnalysisResult analysisResult = getDtoByDocumentIdOrThrow(id);
        return AnalysisResultInfoDto.fromEntity(analysisResult);
    }

    private AnalysisResult getDtoByDocumentIdOrThrow(Long id) {
        return analysisResultRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ANALYSIS_RESULT_NOT_EXISTS));
    }

    public List<AnalysisResultInfoDto> findAnalysisResultsByDocumentId(Long documentId) {
        return analysisResultRepository.findByDocumentId(documentId).stream()
                .map(AnalysisResultInfoDto::fromEntity)
                .toList();
    }

    @Transactional
    public void deleteById(Long id) {
        analysisResultRepository.deleteById(findByIdOrThrow(id).getAnalysisId());
    }

    private AnalysisResult findByIdOrThrow(Long id) {
        return analysisResultRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ANALYSIS_RESULT_NOT_EXISTS));
    }

}
