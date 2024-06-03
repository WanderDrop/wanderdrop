package com.wanderdrop.wserver.controller;

import com.wanderdrop.wserver.dto.AttractionDto;
import com.wanderdrop.wserver.dto.CommentDto;
import com.wanderdrop.wserver.service.comment.CommentService;
import com.wanderdrop.wserver.service.comment.CommentServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentServiceImpl commentServiceImpl;

    public CommentController(CommentService commentService, CommentServiceImpl commentServiceImpl) {
        this.commentService = commentService;
        this.commentServiceImpl = commentServiceImpl;
    }

    @GetMapping("/attraction/{attractionId}")
    public List<CommentDto> getAllActiveComments(@PathVariable Long attractionId) {
        return commentService.getAllActiveComments(attractionId);
    }

    @GetMapping("/user")
    public List<CommentDto> getCommentsForCurrentUser() {
        return commentServiceImpl.getCommentsForCurrentUser();
    }

    @GetMapping("/{id}")
    public CommentDto getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    @PostMapping("/{attractionId}")
    public CommentDto createComment(@RequestBody CommentDto commentDto, @PathVariable Long attractionId) {
        return commentService.createComment(commentDto, attractionId);
    }

    @PutMapping("/{id}")
    public CommentDto updateComment(@PathVariable Long id, @RequestBody CommentDto commentDto) {
        return commentService.updateComment(id, commentDto);
    }

    @PutMapping("/{id}/{reasonId}")
    public void deleteComment(@PathVariable Long id, @PathVariable Long reasonId) {
        commentService.deleteComment(id, reasonId);
    }
}