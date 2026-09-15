package com.yanndub.gpstracker.entities;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

import java.time.Instant;

@Entity
@Table(name = "gps_positions", indexes = {
        @Index(name = "idx_device_recorded_at", columnList = "deviceId, recordedAt")
})
public class GpsPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String deviceId;

    @Column(columnDefinition = "geometry(Point,4326)", nullable = false)
    private Point location;

    private Double speed;
    private Double batteryLevel;

    @Column(nullable = false)
    private Instant recordedAt;

    private boolean alertTriggered;

    public GpsPosition() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(final String deviceId) {
        this.deviceId = deviceId;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(final Point location) {
        this.location = location;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(final Double speed) {
        this.speed = speed;
    }

    public Double getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(final Double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(final Instant recordedAt) {
        this.recordedAt = recordedAt;
    }

    public boolean isAlertTriggered() {
        return alertTriggered;
    }

    public void setAlertTriggered(final boolean alertTriggered) {
        this.alertTriggered = alertTriggered;
    }

    public double getLongitude() {
        return location == null ? 0.0 : location.getX();
    }

    public double getLatitude() {
        return location == null ? 0.0 : location.getY();
    }
}
