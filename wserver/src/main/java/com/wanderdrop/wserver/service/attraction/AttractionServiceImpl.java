package com.wanderdrop.wserver.service.attraction;

import com.wanderdrop.wserver.dto.AttractionDto;
import com.wanderdrop.wserver.mapper.AttractionMapper;
import com.wanderdrop.wserver.model.*;
import com.wanderdrop.wserver.repository.AttractionRepository;
import com.wanderdrop.wserver.repository.DeletionReasonRepository;
import com.wanderdrop.wserver.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttractionServiceImpl implements AttractionService {

    private final AttractionRepository attractionRepository;
    private final UserRepository userRepository;
    private final DeletionReasonRepository deletionReasonRepository;
    private final AttractionMapper attractionMapper;

    public AttractionServiceImpl(AttractionRepository attractionRepository, UserRepository userRepository, DeletionReasonRepository deletionReasonRepository, AttractionMapper attractionMapper) {
        this.attractionRepository = attractionRepository;
        this.userRepository = userRepository;
        this.deletionReasonRepository = deletionReasonRepository;
        this.attractionMapper = attractionMapper;
    }

    @Override
    public AttractionDto saveAttraction(AttractionDto attractionDto) {

        User currentUser = getCurrentAuthenticatedUser();
        if (currentUser == null || (currentUser.getRole() != Role.ADMIN && currentUser.getRole() != Role.USER)) {
            throw new AccessDeniedException("Only logged in users and admins can create an attraction.");
        }

        attractionDto.setCreatedBy(currentUser.getUserId());
        Attraction attraction = attractionMapper.mapToAttraction(attractionDto, userRepository);
        attraction.setCreatedBy(currentUser);
        attraction.setStatus(Status.ACTIVE);
        attraction.setCreatedAt(LocalDateTime.now());
        Attraction savedAttraction = attractionRepository.save(attraction);
        return attractionMapper.mapToAttractionDto(savedAttraction);
    }

    @Override
    public AttractionDto getAttractionById(Long attractionId) {
        Attraction attraction = attractionRepository.findById(attractionId).orElse(null);
        return attraction != null ? attractionMapper.mapToAttractionDto(attraction) : null;
    }

    @Override
    public List<AttractionDto> getAllAttractions() {
        return attractionRepository.findAll().stream()
                .filter(attraction -> attraction.getStatus() == Status.ACTIVE)
                .map(attractionMapper::mapToAttractionDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttractionDto> getAttractionsForCurrentUser() {
        User currentUser = getCurrentAuthenticatedUser();
        if (currentUser == null) {
            throw new AccessDeniedException("User not authenticated.");
        }
        List<Attraction> attractions = attractionRepository.findByCreatedBy(currentUser);
        return attractions.stream()
                .map(attractionMapper::mapToAttractionDto)
                .collect(Collectors.toList());
    }

    @Override
    public AttractionDto updateAttraction(Long attractionId, AttractionDto updatedAttractionDto) {

        User currentUser = checkAdminUser();

        Optional<Attraction> existingAttractionOptional = attractionRepository.findById(attractionId);
        if (existingAttractionOptional.isPresent()) {
            Attraction existingAttraction = existingAttractionOptional.get();

            existingAttraction.setName(updatedAttractionDto.getName());
            existingAttraction.setDescription(updatedAttractionDto.getDescription());
            existingAttraction.setLatitude(updatedAttractionDto.getLatitude());
            existingAttraction.setLongitude(updatedAttractionDto.getLongitude());
            existingAttraction.setStatus(Status.ACTIVE);

            existingAttraction.setUpdatedBy(currentUser);
            existingAttraction.setUpdatedAt(LocalDateTime.now());

            Attraction updatedAttraction = attractionRepository.save(existingAttraction);
            return attractionMapper.mapToAttractionDto(updatedAttraction);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteAttraction(Long attractionId, Long deletionReasonId) {
        User currentUser = checkAdminUser();
        Optional<Attraction> optionalAttraction = attractionRepository.findById(attractionId);
        if (optionalAttraction.isPresent()) {
            Attraction attraction = optionalAttraction.get();
            attraction.setStatus(Status.DELETED);
            DeletionReason deletionReason = deletionReasonRepository.findById(deletionReasonId).orElseThrow(() -> new IllegalArgumentException("Deletion reason not found"));
            attraction.setDeletionReason(deletionReason);
            attraction.setUpdatedBy(getCurrentAuthenticatedUser());
            attraction.setUpdatedAt(LocalDateTime.now());
            attractionRepository.save(attraction);
        } else {
            throw new IllegalArgumentException("Attraction with id " + attractionId + " not found");
        }
    }

    private User getCurrentAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username).orElse(null);
    }

    private User checkAdminUser() {
        User currentUser = getCurrentAuthenticatedUser();
        if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only admins can perform this action.");
        }
        return currentUser;
    }
}