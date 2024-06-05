package com.wanderdrop.wserver.service.attraction;

import com.wanderdrop.wserver.dto.AttractionDto;
import com.wanderdrop.wserver.mapper.AttractionMapper;
import com.wanderdrop.wserver.model.*;
import com.wanderdrop.wserver.repository.AttractionRepository;
import com.wanderdrop.wserver.repository.DeletionReasonRepository;
import com.wanderdrop.wserver.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttractionServiceImplTest {

    @Mock
    private AttractionRepository attractionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DeletionReasonRepository deletionReasonRepository;

    @Mock
    private AttractionMapper attractionMapper;

    @InjectMocks
    private AttractionServiceImpl attractionService;

    private User mockAdminUser;
    private User mockRegularUser;

    @BeforeEach
    public void setUp() {
        mockAdminUser = new User();
        mockAdminUser.setUserId(UUID.randomUUID());
        mockAdminUser.setEmail("admin@example.com");
        mockAdminUser.setRole(Role.ADMIN);

        mockRegularUser = new User();
        mockRegularUser.setUserId(UUID.randomUUID());
        mockRegularUser.setEmail("user@example.com");
        mockRegularUser.setRole(Role.USER);
    }

    private void setAuthenticatedUser(User user) {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(user.getEmail());
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    }

    @Test
    public void testSaveAttraction_AsAdmin() {
        setAuthenticatedUser(mockAdminUser);

        AttractionDto attractionDto = new AttractionDto();
        attractionDto.setName("Test Attraction");
        attractionDto.setDescription("Description");

        Attraction attraction = new Attraction();
        attraction.setAttractionId(1L);
        attraction.setName("Test Attraction");
        attraction.setDescription("Description");
        attraction.setStatus(Status.ACTIVE);
        attraction.setCreatedBy(mockAdminUser);
        attraction.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        when(attractionMapper.mapToAttraction(any(AttractionDto.class), any(UserRepository.class))).thenReturn(attraction);
        when(attractionRepository.save(any(Attraction.class))).thenReturn(attraction);
        when(attractionMapper.mapToAttractionDto(any(Attraction.class))).thenReturn(attractionDto);

        AttractionDto savedAttraction = attractionService.saveAttraction(attractionDto);

        assertNotNull(savedAttraction);
        assertEquals(attraction.getName(), savedAttraction.getName());
        verify(attractionRepository, times(1)).save(any(Attraction.class));
    }

    @Test
    public void testSaveAttraction_AsUser() {
        setAuthenticatedUser(mockRegularUser);

        AttractionDto attractionDto = new AttractionDto();
        attractionDto.setName("Test Attraction");
        attractionDto.setDescription("Description");

        Attraction attraction = new Attraction();
        attraction.setAttractionId(1L);
        attraction.setName("Test Attraction");
        attraction.setDescription("Description");
        attraction.setStatus(Status.ACTIVE);
        attraction.setCreatedBy(mockRegularUser);
        attraction.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        when(attractionMapper.mapToAttraction(any(AttractionDto.class), any(UserRepository.class))).thenReturn(attraction);
        when(attractionRepository.save(any(Attraction.class))).thenReturn(attraction);
        when(attractionMapper.mapToAttractionDto(any(Attraction.class))).thenReturn(attractionDto);

        AttractionDto savedAttraction = attractionService.saveAttraction(attractionDto);

        assertNotNull(savedAttraction);
        assertEquals(attraction.getName(), savedAttraction.getName());
        verify(attractionRepository, times(1)).save(any(Attraction.class));
    }

    @Test
    public void testSaveAttraction_Unauthorized() {
        setAuthenticatedUser(new User());

        AttractionDto attractionDto = new AttractionDto();

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            attractionService.saveAttraction(attractionDto);
        });

        assertEquals("Only logged in users and admins can create an attraction.", exception.getMessage());
        verify(attractionRepository, never()).save(any(Attraction.class));
    }

    @Test
    public void testGetAttractionById() {
        Attraction attraction = new Attraction();
        attraction.setAttractionId(1L);
        attraction.setName("Test Attraction");
        attraction.setDescription("Description");
        attraction.setStatus(Status.ACTIVE);

        AttractionDto attractionDto = new AttractionDto();
        attractionDto.setAttractionId(1L);
        attractionDto.setName("Test Attraction");
        attractionDto.setDescription("Description");

        when(attractionRepository.findById(1L)).thenReturn(Optional.of(attraction));
        when(attractionMapper.mapToAttractionDto(any(Attraction.class))).thenReturn(attractionDto);

        AttractionDto result = attractionService.getAttractionById(1L);

        assertNotNull(result);
        assertEquals(attraction.getName(), result.getName());
        verify(attractionRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAttractionById_NotFound() {
        when(attractionRepository.findById(1L)).thenReturn(Optional.empty());

        AttractionDto attractionDto = attractionService.getAttractionById(1L);

        assertNull(attractionDto);
        verify(attractionRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllAttractions() {
        Attraction attraction1 = new Attraction();
        attraction1.setAttractionId(1L);
        attraction1.setName("Test Attraction 1");
        attraction1.setDescription("Description 1");
        attraction1.setStatus(Status.ACTIVE);

        Attraction attraction2 = new Attraction();
        attraction2.setAttractionId(2L);
        attraction2.setName("Test Attraction 2");
        attraction2.setDescription("Description 2");
        attraction2.setStatus(Status.ACTIVE);

        AttractionDto attractionDto1 = new AttractionDto();
        attractionDto1.setAttractionId(1L);
        attractionDto1.setName("Test Attraction 1");
        attractionDto1.setDescription("Description 1");

        AttractionDto attractionDto2 = new AttractionDto();
        attractionDto2.setAttractionId(2L);
        attractionDto2.setName("Test Attraction 2");
        attractionDto2.setDescription("Description 2");

        when(attractionRepository.findAll()).thenReturn(Arrays.asList(attraction1, attraction2));
        when(attractionMapper.mapToAttractionDto(attraction1)).thenReturn(attractionDto1);
        when(attractionMapper.mapToAttractionDto(attraction2)).thenReturn(attractionDto2);

        List<AttractionDto> result = attractionService.getAllAttractions();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(attractionRepository, times(1)).findAll();
    }

    @Test
    public void testGetAttractionsForCurrentUser() {
        setAuthenticatedUser(mockRegularUser);

        Attraction attraction1 = new Attraction();
        attraction1.setAttractionId(1L);
        attraction1.setName("Test Attraction 1");
        attraction1.setDescription("Description 1");
        attraction1.setStatus(Status.ACTIVE);
        attraction1.setCreatedBy(mockRegularUser);
        attraction1.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        Attraction attraction2 = new Attraction();
        attraction2.setAttractionId(2L);
        attraction2.setName("Test Attraction 2");
        attraction2.setDescription("Description 2");
        attraction2.setStatus(Status.ACTIVE);
        attraction2.setCreatedBy(mockRegularUser);
        attraction2.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        AttractionDto attractionDto1 = new AttractionDto();
        attractionDto1.setAttractionId(1L);
        attractionDto1.setName("Test Attraction 1");
        attractionDto1.setDescription("Description 1");

        AttractionDto attractionDto2 = new AttractionDto();
        attractionDto2.setAttractionId(2L);
        attractionDto2.setName("Test Attraction 2");
        attractionDto2.setDescription("Description 2");

        when(attractionRepository.findByCreatedBy(mockRegularUser)).thenReturn(Arrays.asList(attraction1, attraction2));
        when(attractionMapper.mapToAttractionDto(attraction1)).thenReturn(attractionDto1);
        when(attractionMapper.mapToAttractionDto(attraction2)).thenReturn(attractionDto2);

        List<AttractionDto> result = attractionService.getAttractionsForCurrentUser();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Attraction 1", result.get(0).getName());
        assertEquals("Test Attraction 2", result.get(1).getName());
        verify(attractionRepository, times(1)).findByCreatedBy(mockRegularUser);
    }

    @Test
    public void testUpdateAttraction() {
        setAuthenticatedUser(mockAdminUser);

        Attraction existingAttraction = new Attraction();
        existingAttraction.setAttractionId(1L);
        existingAttraction.setName("Old Attraction");
        existingAttraction.setDescription("Old Description");
        existingAttraction.setStatus(Status.ACTIVE);
        existingAttraction.setCreatedBy(mockAdminUser);
        existingAttraction.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        AttractionDto updatedAttractionDto = new AttractionDto();
        updatedAttractionDto.setName("Updated Attraction");
        updatedAttractionDto.setDescription("Updated Description");

        when(attractionRepository.findById(1L)).thenReturn(Optional.of(existingAttraction));
        when(attractionRepository.save(any(Attraction.class))).thenReturn(existingAttraction);
        when(attractionMapper.mapToAttractionDto(any(Attraction.class))).thenReturn(updatedAttractionDto);

        AttractionDto updatedAttraction = attractionService.updateAttraction(1L, updatedAttractionDto);

        assertNotNull(updatedAttraction);
        assertEquals(updatedAttractionDto.getName(), updatedAttraction.getName());
        assertEquals(updatedAttractionDto.getDescription(), updatedAttraction.getDescription());
        verify(attractionRepository, times(1)).findById(1L);
        verify(attractionRepository, times(1)).save(any(Attraction.class));
    }

    @Test
    public void testUpdateAttraction_NotFound() {
        setAuthenticatedUser(mockAdminUser);

        AttractionDto updatedAttractionDto = new AttractionDto();
        updatedAttractionDto.setName("Updated Attraction");
        updatedAttractionDto.setDescription("Updated Description");

        when(attractionRepository.findById(1L)).thenReturn(Optional.empty());

        AttractionDto updatedAttraction = attractionService.updateAttraction(1L, updatedAttractionDto);

        assertNull(updatedAttraction);
        verify(attractionRepository, times(1)).findById(1L);
        verify(attractionRepository, never()).save(any(Attraction.class));
    }

    @Test
    public void testUpdateAttraction_AsRegularUser() {
        setAuthenticatedUser(mockRegularUser);

        Attraction existingAttraction = new Attraction();
        existingAttraction.setAttractionId(1L);
        existingAttraction.setName("Old Attraction");
        existingAttraction.setDescription("Old Description");
        existingAttraction.setStatus(Status.ACTIVE);
        existingAttraction.setCreatedBy(mockRegularUser);
        existingAttraction.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        AttractionDto updatedAttractionDto = new AttractionDto();
        updatedAttractionDto.setName("Updated Attraction");
        updatedAttractionDto.setDescription("Updated Description");

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            attractionService.updateAttraction(1L, updatedAttractionDto);
        });

        assertEquals("Only admins can perform this action.", exception.getMessage());
        verify(attractionRepository, never()).save(any(Attraction.class));
    }

    @Test
    public void testDeleteAttraction() {
        setAuthenticatedUser(mockAdminUser);

        Attraction attraction = new Attraction();
        attraction.setAttractionId(1L);
        attraction.setName("Test Attraction");
        attraction.setDescription("Description");
        attraction.setStatus(Status.ACTIVE);
        attraction.setCreatedBy(mockAdminUser);
        attraction.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        DeletionReason deletionReason = new DeletionReason();
        deletionReason.setDeletionReasonId(1L);
        deletionReason.setReasonMessage("Duplicate");

        when(attractionRepository.findById(1L)).thenReturn(Optional.of(attraction));
        when(deletionReasonRepository.findById(1L)).thenReturn(Optional.of(deletionReason));

        attractionService.deleteAttraction(1L, 1L);

        verify(attractionRepository, times(1)).findById(1L);
        verify(deletionReasonRepository, times(1)).findById(1L);
        verify(attractionRepository, times(1)).save(any(Attraction.class));
    }

    @Test
    public void testDeleteAttraction_NotFound() {
        setAuthenticatedUser(mockAdminUser);

        when(attractionRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            attractionService.deleteAttraction(1L, 1L);
        });

        assertEquals("Attraction with id 1 not found", exception.getMessage());
        verify(attractionRepository, times(1)).findById(1L);
        verify(deletionReasonRepository, never()).findById(anyLong());
        verify(attractionRepository, never()).save(any(Attraction.class));
    }

    @Test
    public void testDeleteAttraction_AsRegularUser() {
        setAuthenticatedUser(mockRegularUser);

        Attraction attraction = new Attraction();
        attraction.setAttractionId(1L);
        attraction.setName("Test Attraction");
        attraction.setDescription("Description");
        attraction.setStatus(Status.ACTIVE);
        attraction.setCreatedBy(mockRegularUser);
        attraction.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        DeletionReason deletionReason = new DeletionReason();
        deletionReason.setDeletionReasonId(1L);
        deletionReason.setReasonMessage("Duplicate");

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            attractionService.deleteAttraction(1L, 1L);
        });

        assertEquals("Only admins can perform this action.", exception.getMessage());
        verify(attractionRepository, never()).save(any(Attraction.class));
    }
}