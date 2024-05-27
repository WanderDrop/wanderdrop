package com.wanderdrop.wserver.repository;

import com.wanderdrop.wserver.model.DeletionReason;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class DeletionReasonRepositoryTest {

    @Autowired
    private DeletionReasonRepository deletionReasonRepository;

    @Test
    public void testSave() {
        DeletionReason deletionReason = new DeletionReason();
        deletionReason.setDeletionReasonId(1L);
        deletionReason.setReasonMessage("Test Reason");
        deletionReason.setDefaultReason(false);

        deletionReason = deletionReasonRepository.save(deletionReason);

        assertNotNull(deletionReasonRepository.findById(deletionReason.getDeletionReasonId()));
    }
}
