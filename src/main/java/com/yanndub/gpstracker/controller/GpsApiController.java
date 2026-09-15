package com.yanndub.gpstracker.controller;

import com.yanndub.gpstracker.dto.GpsPositionDto;
import com.yanndub.gpstracker.repositories.GpsPositionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/positions")
public class GpsApiController {

    private final GpsPositionRepository gpsPositionRepository;

    public GpsApiController(final GpsPositionRepository gpsPositionRepository) {
        this.gpsPositionRepository = gpsPositionRepository;
    }

    @GetMapping("/latest")
    public ResponseEntity<List<GpsPositionDto>> getLatestGpsPosition(@RequestParam(defaultValue = "50") final int limit) {
        final var page = gpsPositionRepository.findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "recordedAt")))
                .stream()
                .map(GpsPositionDto::from)
                .toList();
        return ResponseEntity.ok(page);
    }
}
