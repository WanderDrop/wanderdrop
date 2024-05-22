package com.wanderdrop.wserver.controller;

import com.wanderdrop.wserver.dto.DeletionReasonDto;
import com.wanderdrop.wserver.service.deletionreason.DeletionReasonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/deletion-reasons")
public class DeletionReasonController {
    private final DeletionReasonService deletionReasonService;

    public DeletionReasonController(DeletionReasonService deletionReasonService) {
        this.deletionReasonService = deletionReasonService;
    }

    @GetMapping
    public List<DeletionReasonDto> getAllDeletionReasons() {
        return deletionReasonService.getDefaultDeletionReasons();
    }

}
