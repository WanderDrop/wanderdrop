package com.wanderdrop.wserver.mapper;

import com.wanderdrop.wserver.dto.CommentDto;
import com.wanderdrop.wserver.model.*;
import com.wanderdrop.wserver.repository.DeletionReasonRepository;
import com.wanderdrop.wserver.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    @InjectMocks
    private CommentMapper commentMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DeletionReasonRepository deletionReasonRepository;

    @Test
    public void testMapToCommentDto() {
        User user = new User();
        user.setEmail("testUser");

        DeletionReason deletionReason = new DeletionReason();
        deletionReason.setDeletionReasonId(1L);

        Attraction attraction = new Attraction();
        attraction.setAttractionId(2L);

        Comment comment = new Comment();
        comment.setCommentId(3L);
        comment.setCommentHeading("Heading");
        comment.setCommentText("Text");
        comment.setCreatedBy(user);
        comment.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        comment.setDeletionReason(deletionReason);
        comment.setAttraction(attraction);

        CommentDto commentDto = commentMapper.mapToCommentDto(comment);

        assertEquals(comment.getCommentId(), commentDto.getCommentId());
        assertEquals(comment.getCommentHeading(), commentDto.getCommentHeading());
        assertEquals(comment.getCommentText(), commentDto.getCommentText());
        assertEquals(comment.getCreatedBy().getUsername(), commentDto.getCreatedBy());
        assertEquals(comment.getCreatedAt(), commentDto.getCreatedAt());
        assertEquals(comment.getDeletionReason().getDeletionReasonId(), commentDto.getDeletionReason());
        assertEquals(comment.getAttraction().getAttractionId(), commentDto.getAttractionId());
    }

    @Test
    public void testMapToComment() {
        CommentDto commentDto = new CommentDto(
                3L, "Heading", "Text", "testUser", Timestamp.valueOf(LocalDateTime.now()), 1L, 2L, Status.ACTIVE
        );

        User user = new User();
        user.setEmail("testUser");

        DeletionReason deletionReason = new DeletionReason();
        deletionReason.setDeletionReasonId(1L);

        when(userRepository.findByEmail("testUser")).thenReturn(Optional.of(user));
        when(deletionReasonRepository.findById(1L)).thenReturn(Optional.of(deletionReason));

        Comment comment = commentMapper.mapToComment(commentDto, userRepository, deletionReasonRepository);

        assertEquals(commentDto.getCommentId(), comment.getCommentId());
        assertEquals(commentDto.getCommentHeading(), comment.getCommentHeading());
        assertEquals(commentDto.getCommentText(), comment.getCommentText());
        assertEquals(commentDto.getCreatedBy(), comment.getCreatedBy().getUsername());
        assertEquals(commentDto.getCreatedAt(), comment.getCreatedAt());
        assertEquals(commentDto.getDeletionReason(), comment.getDeletionReason().getDeletionReasonId());
    }
}