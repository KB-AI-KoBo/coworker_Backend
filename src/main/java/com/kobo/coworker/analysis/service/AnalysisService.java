package com.kobo.coworker.analysis.service;

import com.kobo.coworker.analysis.domain.AnalysisResult;
import com.kobo.coworker.analysis.dto.AnalysisResultInfoDto;
import com.kobo.coworker.analysis.repository.AnalysisResultRepository;
import com.kobo.coworker.common.apiPayload.code.status.ErrorStatus;
import com.kobo.coworker.common.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final AnalysisResultRepository analysisResultRepository;

    @Transactional
    public Long save(AnalysisResultInfoDto dto) {
        AnalysisResult entity = dto.toEntity();
        analysisResultRepository.save(entity);
        return entity.getAnalysisId();
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
