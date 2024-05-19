package com.wanderdrop.wserver.mapper;

import com.wanderdrop.wserver.dto.AttractionDto;
import com.wanderdrop.wserver.model.Attraction;

public class AttractionMapper {

    public static AttractionDto mapToAttractionDto(Attraction attraction) {
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

    public static Attraction mapToAttraction(AttractionDto attractionDto) {
        Attraction attraction = new Attraction();
        attraction.setAttractionId(attractionDto.getAttractionId());
        attraction.setName(attractionDto.getName());
        attraction.setDescription(attractionDto.getDescription());
        attraction.setLatitude(attractionDto.getLatitude());
        attraction.setLongitude(attractionDto.getLongitude());
        attraction.setStatus(attractionDto.getStatus());
        // createdBy, updatedBy, and deletionReason will be set in the service

        return attraction;
    }
}
