package com.wanderdrop.wserver.services.attraction;

import com.wanderdrop.wserver.dto.AttractionDto;
import com.wanderdrop.wserver.mapper.AttractionMapper;
import com.wanderdrop.wserver.model.*;
import com.wanderdrop.wserver.repository.AttractionRepository;
import com.wanderdrop.wserver.repository.DeletionReasonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttractionServiceImpl implements AttractionService {

    private final AttractionRepository attractionRepository;
    private final DeletionReasonRepository deletionReasonRepository;

    public AttractionServiceImpl(AttractionRepository attractionRepository, DeletionReasonRepository deletionReasonRepository) {
        this.attractionRepository = attractionRepository;
        this.deletionReasonRepository = deletionReasonRepository;
    }

    @Override
    public AttractionDto saveAttraction(AttractionDto attractionDto) {
        return null;
    }

    @Override
    public AttractionDto getAttractionById(Long attractionId) {
        Attraction attraction = attractionRepository.findById(attractionId).orElse(null);
        return attraction != null ? AttractionMapper.mapToAttractionDto(attraction) : null;
    }

    @Override
    public List<AttractionDto> getAllAttractions() {
        return attractionRepository.findAll().stream()
                .filter(attraction -> attraction.getStatus() == Status.ACTIVE)
                .map(AttractionMapper::mapToAttractionDto)
                .collect(Collectors.toList());
    }

    @Override
    public AttractionDto updateAttraction(Long attractionId, AttractionDto updatedAttraction) {
            Attraction existingAttraction = attractionRepository.findById(attractionId).orElse(null);
            if (existingAttraction != null) {
                Attraction attractionToUpdate = AttractionMapper.mapToAttraction(updatedAttraction);
                attractionToUpdate.setAttractionId(attractionId);
                return saveAttraction(AttractionMapper.mapToAttractionDto(attractionToUpdate));
            } else {
                return null;
            }
    }

    @Override
    public void deleteAttraction(Long attractionId, Long deletionReasonId) {
        Optional<Attraction> optionalAttraction = attractionRepository.findById(attractionId);
        if (optionalAttraction.isPresent()) {
            Attraction attraction = optionalAttraction.get();
            attraction.setStatus(Status.DELETED);
            DeletionReason deletionReason = deletionReasonRepository.findById(deletionReasonId).orElse(null);
            attraction.setDeletionReason(deletionReason);
            attractionRepository.save(attraction);
        } else {
            throw new IllegalArgumentException("Attraction with id " + attractionId + " not found");
        }
    }
}