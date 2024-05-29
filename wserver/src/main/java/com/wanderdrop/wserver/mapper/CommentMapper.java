package com.wanderdrop.wserver.mapper;

import com.wanderdrop.wserver.dto.CommentDto;
import com.wanderdrop.wserver.model.Comment;
import com.wanderdrop.wserver.model.DeletionReason;
import com.wanderdrop.wserver.model.Status;
import com.wanderdrop.wserver.model.User;
import com.wanderdrop.wserver.repository.DeletionReasonRepository;
import com.wanderdrop.wserver.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentDto mapToCommentDto(Comment comment) {
        return new CommentDto(
                comment.getCommentId(),
                comment.getCommentHeading(),
                comment.getCommentText(),
                comment.getCreatedBy() != null ? comment.getCreatedBy().getUsername() : null,
                comment.getCreatedAt(),
                comment.getDeletionReason() != null ? comment.getDeletionReason().getReasonMessage() : null
        );
    }

    public Comment mapToComment(CommentDto commentDto, UserRepository userRepository, DeletionReasonRepository deletionReasonRepository) {
        Comment comment = new Comment();
        comment.setCommentId(commentDto.getCommentId());
        comment.setCommentHeading(commentDto.getCommentHeading());
        comment.setCommentText(commentDto.getCommentText());
        comment.setStatus(Status.ACTIVE);

        if (commentDto.getCreatedBy() != null) {
            User createdBy = userRepository.findByEmail(commentDto.getCreatedBy()).orElse(null);
            comment.setCreatedBy(createdBy);
        }

        if (commentDto.getDeletionReason() != null) {
            DeletionReason deletionReason = deletionReasonRepository.findByReasonMessage(commentDto.getDeletionReason()).orElse(null);
            comment.setDeletionReason(deletionReason);
        }

        comment.setCreatedAt(commentDto.getCreatedAt());
        return comment;
    }
}
