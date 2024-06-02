package com.wanderdrop.wserver.repository;

import com.wanderdrop.wserver.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttractionRepository attractionRepository;

    private User user;
    private Attraction attraction;

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
        userRepository.save(user);

        attraction = new Attraction();
        attraction.setName("Test Attraction");
        attraction.setDescription("Description");
        attraction.setLatitude(21.12345);
        attraction.setLongitude(21.12345);
        attraction.setCreatedBy(user);
        attraction.setStatus(Status.ACTIVE);

        attractionRepository.save(attraction);
    }

    @Test
    public void testFindByStatusAndAttraction_AttractionId() {
        Comment comment1 = new Comment();
        comment1.setCommentHeading("Heading1");
        comment1.setCommentText("Text1");
        comment1.setStatus(Status.ACTIVE);
        comment1.setAttraction(attraction);
        comment1.setCreatedBy(user);
        comment1.setCreatedAt(Timestamp.from(Instant.now()));
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setCommentHeading("Heading2");
        comment2.setCommentText("Text2");
        comment2.setStatus(Status.DELETED);
        comment2.setAttraction(attraction);
        comment2.setCreatedBy(user);
        comment2.setCreatedAt(Timestamp.from(Instant.now()));
        commentRepository.save(comment2);

        Comment comment3 = new Comment();
        comment3.setCommentHeading("Heading3");
        comment3.setCommentText("Text3");
        comment3.setStatus(Status.ACTIVE);
        comment3.setAttraction(attraction);
        comment3.setCreatedBy(user);
        comment3.setCreatedAt(Timestamp.from(Instant.now()));
        commentRepository.save(comment3);

        List<Comment> activeComments = commentRepository.findByStatusAndAttraction_AttractionId(Status.ACTIVE, attraction.getAttractionId());

        assertNotNull(activeComments);
        assertEquals(2, activeComments.size());
        assertEquals("Heading1", activeComments.get(0).getCommentHeading());
        assertEquals("Heading3", activeComments.get(1).getCommentHeading());
    }
}
