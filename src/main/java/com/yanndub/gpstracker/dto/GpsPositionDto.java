package com.yanndub.gpstracker.dto;

import com.yanndub.gpstracker.entities.GpsPosition;

import java.time.Instant;

public record GpsPositionDto(String deviceId, double latitude, double longitude, double speed, double batteryLevel, Instant recordedAt) {
    public static GpsPositionDto from(final GpsPosition gpsPosition) {
        return new GpsPositionDto(gpsPosition.getDeviceId(), gpsPosition.getLatitude(), gpsPosition.getLongitude(), gpsPosition.getSpeed(), gpsPosition.getBatteryLevel(), gpsPosition.getRecordedAt());
    }
}
