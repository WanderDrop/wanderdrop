package com.wanderdrop.wserver.repository;

import com.wanderdrop.wserver.model.DeletionReason;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class DeletionReasonRepositoryTest {

    @Autowired
    private DeletionReasonRepository deletionReasonRepository;

    private DeletionReason deletionReason;

    @BeforeEach
    void setUp() {
        deletionReason = new DeletionReason();
        deletionReason.setDeletionReasonId(1L);
        deletionReason.setReasonMessage("Test Reason");
        deletionReason.setDefaultReason(false);

        deletionReason = deletionReasonRepository.save(deletionReason);
    }

    @AfterEach
    public void tearDown() {
        deletionReasonRepository.deleteAll();
    }

    @Test
    public void testSave() {
        assertNotNull(deletionReasonRepository.findById(deletionReason.getDeletionReasonId()));
    }

    @Test
    public void testSaveAndGetById() {

        Optional<DeletionReason> foundDeletionReason = deletionReasonRepository.findById(deletionReason.getDeletionReasonId());

        assertTrue(foundDeletionReason.isPresent());
        assertEquals(deletionReason, foundDeletionReason.get());
    }

    @Test
    public void testFindAll() {
        List<DeletionReason> deletionReasons = deletionReasonRepository.findAll();
        assertNotNull(deletionReasons);
        assertFalse(deletionReasons.isEmpty());
    }

    @Test
    public void testUpdate() {

        deletionReason.setReasonMessage("Updated Test Reason");
        deletionReasonRepository.save(deletionReason);

        Optional<DeletionReason> updatedDeletionReason = deletionReasonRepository.findById(deletionReason.getDeletionReasonId());

        assertTrue(updatedDeletionReason.isPresent());
        assertEquals("Updated Test Reason", updatedDeletionReason.get().getReasonMessage());
    }

    @Test
    public void testDelete() {
        deletionReasonRepository.delete(deletionReason);

        Optional<DeletionReason> deletedDeletionReason = deletionReasonRepository.findById(deletionReason.getDeletionReasonId());

        assertFalse(deletedDeletionReason.isPresent());
    }
}
