package com.yanndub.gpstracker.controller;

import com.yanndub.gpstracker.dto.TraccarPayload;
import com.yanndub.gpstracker.services.GpsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gps")
public class GpsController {

    private final GpsService gpsService;

    public GpsController(final GpsService gpsService) {
        this.gpsService = gpsService;
    }

    @PostMapping
    public ResponseEntity<Void> handleGpsPing(final TraccarPayload payload) {
        gpsService.processAndSavePosition(payload);
        return ResponseEntity.ok().build();
    }
}
