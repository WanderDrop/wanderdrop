package com.wanderdrop.wserver.service.deletionreason;

import com.wanderdrop.wserver.dto.DeletionReasonDto;
import com.wanderdrop.wserver.mapper.DeletionReasonMapper;
import com.wanderdrop.wserver.model.DeletionReason;
import com.wanderdrop.wserver.repository.DeletionReasonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletionReasonServiceImplTest {

    @Mock
    private DeletionReasonRepository deletionReasonRepository;

    @Mock
    private DeletionReasonMapper deletionReasonMapper;

    @InjectMocks
    private DeletionReasonServiceImpl deletionReasonService;

    private DeletionReason mockDefaultReason;
    private DeletionReason mockCustomReason;
    private DeletionReasonDto mockDefaultReasonDto;
    private DeletionReasonDto mockCustomReasonDto;

    @BeforeEach
    public void setUp() {
        mockDefaultReason = new DeletionReason();
        mockDefaultReason.setDeletionReasonId(1L);
        mockDefaultReason.setReasonMessage("Default Reason");
        mockDefaultReason.setDefaultReason(true);

        mockCustomReason = new DeletionReason();
        mockCustomReason.setDeletionReasonId(2L);
        mockCustomReason.setReasonMessage("Custom Reason");
        mockCustomReason.setDefaultReason(false);

        mockDefaultReasonDto = new DeletionReasonDto();
        mockDefaultReasonDto.setId(1L);
        mockDefaultReasonDto.setReasonMessage("Default Reason");

        mockCustomReasonDto = new DeletionReasonDto();
        mockCustomReasonDto.setId(2L);
        mockCustomReasonDto.setReasonMessage("Custom Reason");
    }

    @Test
    public void testGetDefaultDeletionReasons() {
        when(deletionReasonRepository.findAll()).thenReturn(Arrays.asList(mockDefaultReason, mockCustomReason));
        when(deletionReasonMapper.mapToDeletionReasonDto(mockDefaultReason)).thenReturn(mockDefaultReasonDto);

        List<DeletionReasonDto> result = deletionReasonService.getDefaultDeletionReasons();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockDefaultReasonDto.getReasonMessage(), result.getFirst().getReasonMessage());
        verify(deletionReasonRepository, times(1)).findAll();
    }

    @Test
    public void testSaveDeletionReason() {
        when(deletionReasonMapper.mapToDeletionReason(any(DeletionReasonDto.class))).thenReturn(mockCustomReason);
        when(deletionReasonRepository.save(any(DeletionReason.class))).thenReturn(mockCustomReason);
        when(deletionReasonMapper.mapToDeletionReasonDto(any(DeletionReason.class))).thenReturn(mockCustomReasonDto);

        DeletionReasonDto savedDeletionReason = deletionReasonService.saveDeletionReason(mockCustomReasonDto);

        assertNotNull(savedDeletionReason);
        assertEquals(mockCustomReasonDto.getReasonMessage(), savedDeletionReason.getReasonMessage());
        verify(deletionReasonRepository, times(1)).save(any(DeletionReason.class));
    }
}