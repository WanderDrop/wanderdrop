package com.wanderdrop.wserver.repository;

import com.wanderdrop.wserver.model.Comment;
import com.wanderdrop.wserver.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByStatus(Status status);
}
