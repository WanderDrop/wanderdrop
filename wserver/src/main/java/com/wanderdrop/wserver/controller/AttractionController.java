package com.wanderdrop.wserver.controller;

import com.wanderdrop.wserver.dto.AttractionDto;
import com.wanderdrop.wserver.services.attraction.AttractionServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attractions")
public class AttractionController {
    private final AttractionServiceImpl attractionServiceImpl;

    public AttractionController(AttractionServiceImpl attractionServiceImpl) {
        this.attractionServiceImpl = attractionServiceImpl;
    }

    @GetMapping
    public List<AttractionDto> getAllAttractions() {
        return attractionServiceImpl.getAllAttractions();
    }

    @GetMapping("/{id}")
    public AttractionDto getAttractionById(@PathVariable Long id) {
        return attractionServiceImpl.getAttractionById(id);
    }

    @PostMapping
    public AttractionDto createAttraction(@RequestBody AttractionDto attractionDTO) {
        return attractionServiceImpl.saveAttraction(attractionDTO);
    }

    @PutMapping("/{id}")
    public AttractionDto updateAttraction(@PathVariable Long id, @RequestBody AttractionDto attractionDTO) {
        return attractionServiceImpl.updateAttraction(id, attractionDTO);
    }

    @PutMapping("/{id}/{reasonId}")
    public void deleteAttraction(@PathVariable Long id, @PathVariable Long reasonId) {
        attractionServiceImpl.deleteAttraction(id, reasonId);
    }
}
