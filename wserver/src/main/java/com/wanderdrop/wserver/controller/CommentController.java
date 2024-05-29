package com.wanderdrop.wserver.controller;

import com.wanderdrop.wserver.dto.CommentDto;
import com.wanderdrop.wserver.service.comment.CommentService;
import com.wanderdrop.wserver.service.comment.CommentServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentDto> getAllActiveComments() {
        return commentService.getAllActiveComments();
    }

    @GetMapping("/{id}")
    public CommentDto getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    @PostMapping
    public CommentDto createComment(@RequestBody CommentDto commentDto) {
        return commentService.createComment(commentDto);
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