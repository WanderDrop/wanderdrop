package com.wanderdrop.wserver.mapper;

import com.wanderdrop.wserver.dto.AttractionDto;
import com.wanderdrop.wserver.model.Attraction;
import com.wanderdrop.wserver.model.User;
import com.wanderdrop.wserver.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class AttractionMapper {

    public AttractionDto mapToAttractionDto(Attraction attraction) {
        return new AttractionDto(
                attraction.getAttractionId(),
                attraction.getName(),
                attraction.getDescription(),
                attraction.getLatitude(),
                attraction.getLongitude(),
                attraction.getStatus(),
                attraction.getCreatedBy() != null ? attraction.getCreatedBy().getUserId() : null,
                attraction.getUpdatedBy() != null ? attraction.getUpdatedBy().getUserId() : null,
                attraction.getCreatedAt(),
                attraction.getUpdatedAt(),
                attraction.getDeletionReason() != null ? attraction.getDeletionReason().getDeletionReasonId() : null

        );
    }

    public Attraction mapToAttraction(AttractionDto attractionDto, UserRepository userRepository) {
        Attraction attraction = new Attraction();
        attraction.setAttractionId(attractionDto.getAttractionId());
        attraction.setName(attractionDto.getName());
        attraction.setDescription(attractionDto.getDescription());
        attraction.setLatitude(attractionDto.getLatitude());
        attraction.setLongitude(attractionDto.getLongitude());
        attraction.setStatus(attractionDto.getStatus());
        if (attractionDto.getCreatedBy() != null) {
            User createdBy = userRepository.findById(attractionDto.getCreatedBy()).orElse(null);
            attraction.setCreatedBy(createdBy);
        }
        if (attractionDto.getUpdatedBy() != null) {
            User updatedBy = userRepository.findById(attractionDto.getUpdatedBy()).orElse(null);
            attraction.setUpdatedBy(updatedBy);
        }
        attraction.setCreatedAt(attractionDto.getCreatedAt());
        attraction.setUpdatedAt(attractionDto.getUpdatedAt());

        return attraction;
    }
}
