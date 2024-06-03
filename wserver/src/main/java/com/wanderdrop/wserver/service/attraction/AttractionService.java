package com.wanderdrop.wserver.service.attraction;

import com.wanderdrop.wserver.dto.AttractionDto;

import java.util.List;

public interface AttractionService {
    AttractionDto saveAttraction(AttractionDto attractionDTO);
    AttractionDto getAttractionById(Long attractionId);
    List<AttractionDto> getAllAttractions();
    List<AttractionDto> getAttractionsForCurrentUser();
    AttractionDto updateAttraction(Long attractionId, AttractionDto updatedAttraction);
    void deleteAttraction(Long attractionId, Long deletionReasonId);
}
