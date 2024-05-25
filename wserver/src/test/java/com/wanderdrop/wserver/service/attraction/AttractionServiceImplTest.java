package com.wanderdrop.wserver.service.attraction;

import com.wanderdrop.wserver.dto.AttractionDto;
import com.wanderdrop.wserver.model.Attraction;
import com.wanderdrop.wserver.model.Role;
import com.wanderdrop.wserver.model.Status;
import com.wanderdrop.wserver.model.User;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttractionServiceImplTest {

    @Mock
    private AttractionRepository attractionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DeletionReasonRepository deletionReasonRepository;

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

        when(attractionRepository.save(any(Attraction.class))).thenReturn(attraction);

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

        when(attractionRepository.save(any(Attraction.class))).thenReturn(attraction);

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

}