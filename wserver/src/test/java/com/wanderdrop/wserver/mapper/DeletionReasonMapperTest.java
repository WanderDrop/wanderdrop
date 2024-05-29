package com.wanderdrop.wserver.mapper;

import com.wanderdrop.wserver.dto.DeletionReasonDto;
import com.wanderdrop.wserver.model.DeletionReason;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DeletionReasonMapperTest {

    private final DeletionReasonMapper deletionReasonMapper = new DeletionReasonMapper();

    @Test
    public void testMapToDeletionReasonDto() {
        DeletionReason deletionReason = new DeletionReason();
        deletionReason.setDeletionReasonId(1L);
        deletionReason.setReasonMessage("Test Reason");

        DeletionReasonDto deletionReasonDto = deletionReasonMapper.mapToDeletionReasonDto(deletionReason);

        assertEquals(deletionReason.getDeletionReasonId(), deletionReasonDto.getId());
        assertEquals(deletionReason.getReasonMessage(), deletionReasonDto.getReasonMessage());
    }

    @Test
    public void testMapToDeletionReason() {
        DeletionReasonDto deletionReasonDto = new DeletionReasonDto(1L, "Test Reason");

        DeletionReason deletionReason = deletionReasonMapper.mapToDeletionReason(deletionReasonDto);

        assertEquals(deletionReasonDto.getId(), deletionReason.getDeletionReasonId());
        assertEquals(deletionReasonDto.getReasonMessage(), deletionReason.getReasonMessage());
    }
}