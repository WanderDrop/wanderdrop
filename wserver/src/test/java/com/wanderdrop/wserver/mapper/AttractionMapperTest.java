package com.wanderdrop.wserver.mapper;

import com.wanderdrop.wserver.dto.AttractionDto;
import com.wanderdrop.wserver.model.Attraction;
import com.wanderdrop.wserver.model.Status;
import com.wanderdrop.wserver.model.User;
import com.wanderdrop.wserver.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttractionMapperTest {

    @Mock
    private UserRepository userRepository;

    private final AttractionMapper attractionMapper = new AttractionMapper();

    @Test
    public void testMapToAttractionDto() {
        User createdBy = new User();
        createdBy.setUserId(1L);

        User updatedBy = new User();
        updatedBy.setUserId(2L);

        Attraction attraction = new Attraction();
        attraction.setAttractionId(1L);
        attraction.setName("Test Attraction");
        attraction.setDescription("Description");
        attraction.setLatitude(123.45);
        attraction.setLongitude(67.89);
        attraction.setStatus(Status.ACTIVE);
        attraction.setCreatedBy(createdBy);
        attraction.setUpdatedBy(updatedBy);
        attraction.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        attraction.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        AttractionDto attractionDto = attractionMapper.mapToAttractionDto(attraction);

        assertNotNull(attractionDto);
        assertEquals(attraction.getAttractionId(), attractionDto.getAttractionId());
        assertEquals(attraction.getName(), attractionDto.getName());
        assertEquals(attraction.getDescription(), attractionDto.getDescription());
        assertEquals(attraction.getLatitude(), attractionDto.getLatitude());
        assertEquals(attraction.getLongitude(), attractionDto.getLongitude());
        assertEquals(attraction.getStatus(), attractionDto.getStatus());
        assertEquals(attraction.getCreatedBy().getUserId(), attractionDto.getCreatedBy());
        assertEquals(attraction.getUpdatedBy().getUserId(), attractionDto.getUpdatedBy());
        assertEquals(attraction.getCreatedAt(), attractionDto.getCreatedAt());
        assertEquals(attraction.getUpdatedAt(), attractionDto.getUpdatedAt());
    }

    @Test
    public void testMapToAttraction_UserNotFound() {
        UUID createdById = UUID.randomUUID();
        UUID updatedById = UUID.randomUUID();

        AttractionDto attractionDto = new AttractionDto();
        attractionDto.setAttractionId(1L);
        attractionDto.setName("Test Attraction");
        attractionDto.setDescription("Description");
        attractionDto.setLatitude(123.45);
        attractionDto.setLongitude(67.89);
        attractionDto.setStatus(Status.ACTIVE);
        attractionDto.setCreatedBy(1L);
        attractionDto.setUpdatedBy(2L);
        attractionDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        attractionDto.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Attraction attraction = attractionMapper.mapToAttraction(attractionDto, userRepository);

        assertNotNull(attraction);
        assertNull(attraction.getCreatedBy());
        assertNull(attraction.getUpdatedBy());
    }
}