package com.wanderdrop.wserver.controller;

import com.wanderdrop.wserver.dto.DeletionReasonDto;
import com.wanderdrop.wserver.service.deletionreason.DeletionReasonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deletion-reasons")
public class DeletionReasonController {
    private final DeletionReasonService deletionReasonService;

    public DeletionReasonController(DeletionReasonService deletionReasonService) {
        this.deletionReasonService = deletionReasonService;
    }

    @GetMapping
    public List<DeletionReasonDto> getDefaultDeletionReasons() {
        return deletionReasonService.getDefaultDeletionReasons();
    }

    @PostMapping
    public DeletionReasonDto saveDeletionReason(@RequestBody DeletionReasonDto deletionReasonDto) {
        return deletionReasonService.saveDeletionReason(deletionReasonDto);
    }


}
