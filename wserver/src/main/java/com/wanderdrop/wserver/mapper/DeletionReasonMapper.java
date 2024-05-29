package com.wanderdrop.wserver.mapper;

import com.wanderdrop.wserver.dto.DeletionReasonDto;
import com.wanderdrop.wserver.model.DeletionReason;
import org.springframework.stereotype.Component;

@Component
public class DeletionReasonMapper {

    public DeletionReasonDto mapToDeletionReasonDto(DeletionReason deletionReason) {
        return new DeletionReasonDto(
                deletionReason.getDeletionReasonId(),
                deletionReason.getReasonMessage()
        );
    }

    public DeletionReason mapToDeletionReason(DeletionReasonDto deletionReasonDto) {
        DeletionReason deletionReason = new DeletionReason();
        deletionReason.setDeletionReasonId(deletionReasonDto.getId());
        deletionReason.setReasonMessage(deletionReasonDto.getReasonMessage());
        return deletionReason;
    }
}
