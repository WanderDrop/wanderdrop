package com.wanderdrop.wserver.repository;

import com.wanderdrop.wserver.model.Attraction;
import com.wanderdrop.wserver.model.Comment;
import com.wanderdrop.wserver.model.Status;
import com.wanderdrop.wserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByStatusAndAttraction_AttractionId(Status status, Long attractionId);
    List<Comment> findByCreatedBy(User user);

}
