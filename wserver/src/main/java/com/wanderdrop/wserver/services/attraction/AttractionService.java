package com.wanderdrop.wserver.services.attraction;

import com.wanderdrop.wserver.dto.AttractionDto;

import java.util.List;

public interface AttractionService {
    AttractionDto saveAttraction(AttractionDto attractionDTO);

    AttractionDto getAttractionById(Long attractionId);

    List<AttractionDto> getAllAttractions();

    AttractionDto updateAttraction(Long attractionId, AttractionDto updatedAttraction);

    void deleteAttraction(Long attractionId, Long deletionReasonId);
}
