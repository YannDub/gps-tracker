package com.yanndub.gpstracker.services;

import com.yanndub.gpstracker.dto.TraccarPayload;
import com.yanndub.gpstracker.entities.GpsPosition;
import com.yanndub.gpstracker.repositories.GpsPositionRepository;
import jakarta.transaction.Transactional;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class GpsService {
    private static final Logger LOG = LoggerFactory.getLogger(GpsService.class);

    @Value("${app.geofencing.parking-latitude}")
    private double parkingLatitude;

    @Value("${app.geofencing.parking-longitude}")
    private double parkingLongitude;

    @Value("${app.geofencing.max-radius-meters}")
    private double maxAllowedRadiusMeters;

    private final TelegramNotificationService telegramNotificationService;
    private final GpsPositionRepository gpsPositionRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    public GpsService(final TelegramNotificationService telegramNotificationService, final GpsPositionRepository gpsPositionRepository) {
        this.telegramNotificationService = telegramNotificationService;
        this.gpsPositionRepository = gpsPositionRepository;
    }

    @Transactional
    public GpsPosition processAndSavePosition(final TraccarPayload payload) {
        final var deviceId = payload.id() == null ? "UNKNOWN_DEVICE" : payload.id();
        final var point = createPoint(payload.lat(), payload.lon());
        final var isOutside = gpsPositionRepository.isOutsideGeofence(
                payload.lat(),
                payload.lon(),
                parkingLatitude,
                parkingLongitude,
                maxAllowedRadiusMeters
        );

        if (isOutside) {
            LOG.warn("Véhicule {} sortie du périmètre autorisé ! (Lat: {}, Lon: {})", payload.id(), payload.lat(), payload.lon());

            telegramNotificationService.sendAlert(
                    deviceId,
                    payload.lat(),
                    payload.lon(),
                    payload.getSpeedInKmh(),
                    payload.batt()
            );
        } else {
            LOG.warn("Position OK pour {} : véhicule dans le périmètre autorisé", payload.id());
        }

        final var gpsPosition = new GpsPosition();
        gpsPosition.setDeviceId(deviceId);
        gpsPosition.setLocation(point);
        gpsPosition.setSpeed(payload.getSpeedInKmh());
        gpsPosition.setBatteryLevel(payload.batt());

        final var recordedAt = payload.timestamp() == null ? Instant.now() : Instant.ofEpochMilli(payload.timestamp() * 1000L);
        gpsPosition.setRecordedAt(recordedAt);

        gpsPosition.setAlertTriggered(false);

        return gpsPositionRepository.save(gpsPosition);
    }

    private Point createPoint(final double lat, final double lon) {
        return geometryFactory.createPoint(new Coordinate(lon, lat));
    }
}
