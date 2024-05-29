package com.wanderdrop.wserver.service.comment;

import com.wanderdrop.wserver.dto.CommentDto;
import com.wanderdrop.wserver.mapper.CommentMapper;
import com.wanderdrop.wserver.model.Comment;
import com.wanderdrop.wserver.model.DeletionReason;
import com.wanderdrop.wserver.model.Status;
import com.wanderdrop.wserver.repository.CommentRepository;
import com.wanderdrop.wserver.repository.DeletionReasonRepository;
import com.wanderdrop.wserver.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
        Comment comment = commentMapper.mapToComment(commentDto, userRepository, deletionReasonRepository);
        comment = commentRepository.save(comment);
        return commentMapper.mapToCommentDto(comment);
    }

    @Override
    public CommentDto updateComment(Long id, CommentDto commentDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setCommentHeading(commentDto.getCommentHeading());
        comment.setCommentText(commentDto.getCommentText());
        comment = commentRepository.save(comment);
        return commentMapper.mapToCommentDto(comment);
    }

    @Override
    public void deleteComment(Long id, String deletionReason) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setStatus(Status.DELETED);
        DeletionReason reason = deletionReasonRepository.findByReasonMessage(deletionReason).orElse(null);
        comment.setDeletionReason(reason);
        commentRepository.save(comment);
    }
}
