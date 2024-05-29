package com.wanderdrop.wserver.service.comment;

import com.wanderdrop.wserver.dto.AttractionDto;
import com.wanderdrop.wserver.dto.CommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> getAllActiveComments();
    CommentDto getCommentById(Long id);
    CommentDto createComment(CommentDto commentDto, Long attractionId);
    CommentDto updateComment(Long id, CommentDto commentDto);
    void deleteComment(Long id, Long deletionReasonId);
}
