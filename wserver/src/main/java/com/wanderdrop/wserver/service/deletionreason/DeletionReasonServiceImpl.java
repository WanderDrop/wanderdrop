package com.wanderdrop.wserver.service.deletionreason;

import com.wanderdrop.wserver.dto.AttractionDto;
import com.wanderdrop.wserver.dto.DeletionReasonDto;
import com.wanderdrop.wserver.mapper.AttractionMapper;
import com.wanderdrop.wserver.mapper.DeletionReasonMapper;
import com.wanderdrop.wserver.model.Status;
import com.wanderdrop.wserver.repository.DeletionReasonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeletionReasonServiceImpl implements DeletionReasonService {
    private final DeletionReasonRepository deletionReasonRepository;
    private final DeletionReasonMapper deletionReasonMapper;

    public DeletionReasonServiceImpl(DeletionReasonRepository deletionReasonRepository, DeletionReasonMapper deletionReasonMapper) {
        this.deletionReasonRepository = deletionReasonRepository;
        this.deletionReasonMapper = deletionReasonMapper;
    }

    @Override
    public List<DeletionReasonDto> getAllDeletionReasons() {
        return deletionReasonRepository.findAll().stream()
                .map(DeletionReasonMapper::mapToDeletionReasonDto)
                .collect(Collectors.toList());
    }
}
