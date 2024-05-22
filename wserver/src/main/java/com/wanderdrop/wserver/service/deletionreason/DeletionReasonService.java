package com.wanderdrop.wserver.service.deletionreason;

import com.wanderdrop.wserver.dto.DeletionReasonDto;

import java.util.List;

public interface DeletionReasonService {
    List<DeletionReasonDto> getDefaultDeletionReasons();
    DeletionReasonDto saveDeletionReason(DeletionReasonDto deletionReasonDto);
}
