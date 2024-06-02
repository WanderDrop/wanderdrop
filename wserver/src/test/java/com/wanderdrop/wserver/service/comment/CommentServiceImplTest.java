package com.wanderdrop.wserver.service.comment;

import com.wanderdrop.wserver.dto.CommentDto;
import com.wanderdrop.wserver.mapper.CommentMapper;
import com.wanderdrop.wserver.model.*;
import com.wanderdrop.wserver.repository.AttractionRepository;
import com.wanderdrop.wserver.repository.CommentRepository;
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
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DeletionReasonRepository deletionReasonRepository;

    @Mock
    private AttractionRepository attractionRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private User user;
    private Attraction attraction;
    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setEmail("testUser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setUpdatedAt(Timestamp.from(Instant.now()));

        attraction = new Attraction();
        attraction.setAttractionId(1L);
        attraction.setName("Test Attraction");
        attraction.setCreatedBy(user);
        attraction.setStatus(Status.ACTIVE);

        comment = new Comment();
        comment.setCommentId(1L);
        comment.setCommentHeading("Heading1");
        comment.setCommentText("Text1");
        comment.setStatus(Status.ACTIVE);
        comment.setAttraction(attraction);
        comment.setCreatedBy(user);
        comment.setCreatedAt(Timestamp.from(Instant.now()));

        commentDto = new CommentDto();
        commentDto.setCommentId(1L);
        commentDto.setCommentHeading("Heading1");
        commentDto.setCommentText("Text1");
        commentDto.setCreatedBy("testUser");
        commentDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        commentDto.setAttractionId(attraction.getAttractionId());
    }

    @Test
    public void testGetAllActiveComments() {
        when(commentRepository.findByStatusAndAttraction_AttractionId(Status.ACTIVE, 1L)).thenReturn(Arrays.asList(comment));
        when(commentMapper.mapToCommentDto(comment)).thenReturn(commentDto);

        List<CommentDto> result = commentService.getAllActiveComments(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Heading1", result.getFirst().getCommentHeading());
        verify(commentRepository, times(1)).findByStatusAndAttraction_AttractionId(Status.ACTIVE, 1L);
    }

    @Test
    public void testGetCommentById() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentMapper.mapToCommentDto(comment)).thenReturn(commentDto);

        CommentDto result = commentService.getCommentById(1L);

        assertNotNull(result);
        assertEquals("Heading1", result.getCommentHeading());
        verify(commentRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateComment() {
        mockSecurityContext(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(attractionRepository.findById(1L)).thenReturn(Optional.of(attraction));
        when(commentMapper.mapToComment(any(CommentDto.class), eq(userRepository), eq(deletionReasonRepository))).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.mapToCommentDto(comment)).thenReturn(commentDto);

        CommentDto result = commentService.createComment(commentDto, 1L);

        assertNotNull(result);
        assertEquals("Heading1", result.getCommentHeading());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    public void testUpdateComment() {
        mockSecurityContext(user.getEmail());
        user.setRole(Role.ADMIN);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.mapToCommentDto(comment)).thenReturn(commentDto);

        CommentDto result = commentService.updateComment(1L, commentDto);

        assertNotNull(result);
        assertEquals("Heading1", result.getCommentHeading());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    public void testDeleteComment() {
        mockSecurityContext(user.getEmail());
        user.setRole(Role.ADMIN);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(deletionReasonRepository.findById(1L)).thenReturn(Optional.of(new DeletionReason()));

        commentService.deleteComment(1L, 1L);

        assertEquals(Status.DELETED, comment.getStatus());
        verify(commentRepository, times(1)).save(comment);
    }

    private void mockSecurityContext(String email) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}