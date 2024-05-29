package com.wanderdrop.wserver.service.comment;

import com.wanderdrop.wserver.dto.CommentDto;
import com.wanderdrop.wserver.mapper.CommentMapper;
import com.wanderdrop.wserver.model.*;
import com.wanderdrop.wserver.repository.CommentRepository;
import com.wanderdrop.wserver.repository.DeletionReasonRepository;
import com.wanderdrop.wserver.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final DeletionReasonRepository deletionReasonRepository;

    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper, UserRepository userRepository, DeletionReasonRepository deletionReasonRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
        this.deletionReasonRepository = deletionReasonRepository;
    }

    @Override
    public List<CommentDto> getAllActiveComments() {
        return commentRepository.findByStatus(Status.ACTIVE)
                .stream()
                .map(commentMapper::mapToCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        return commentMapper.mapToCommentDto(comment);

    }

    @Override
    public CommentDto createComment(CommentDto commentDto) {
        User currentUser = getCurrentAuthenticatedUser();
        if (currentUser == null || (currentUser.getRole() != Role.ADMIN && currentUser.getRole() != Role.USER)) {
            throw new AccessDeniedException("Only logged-in users and admins can create a comment.");
        }

        Comment comment = commentMapper.mapToComment(commentDto, userRepository, deletionReasonRepository);
        comment.setCreatedBy(currentUser);
        comment.setStatus(Status.ACTIVE);
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.mapToCommentDto(savedComment);
    }

    @Override
    public CommentDto updateComment(Long id, CommentDto commentDto) {
        User currentUser = checkAdminUser();

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        comment.setCommentHeading(commentDto.getCommentHeading());
        comment.setCommentText(commentDto.getCommentText());
        comment.setUpdatedBy(currentUser);
        comment.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.mapToCommentDto(updatedComment);
    }

    @Override
    public void deleteComment(Long commentId, Long deletionReasonId) {
        User currentUser = checkAdminUser();

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setStatus(Status.DELETED);
            DeletionReason deletionReason = deletionReasonRepository.findById(deletionReasonId).orElse(null);
            comment.setDeletionReason(deletionReason);

            comment.setUpdatedBy(currentUser);
            comment.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            commentRepository.save(comment);
        } else {
            throw new IllegalArgumentException("Comment with id " + commentId + " not found");
        }
    }

    private User getCurrentAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElse(null);
    }

    private User checkAdminUser() {
        User currentUser = getCurrentAuthenticatedUser();
        if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only admins can perform this action.");
        }
        return currentUser;
    }
}
