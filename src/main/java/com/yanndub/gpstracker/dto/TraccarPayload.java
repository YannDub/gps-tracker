package com.yanndub.gpstracker.dto;

public record TraccarPayload(
        String id,
        double lat,
        double lon,
        Long timestamp,
        Double speed,
        Double bearing,
        Double altitude,
        Double accuracy,
        Double batt,
        Boolean charge
) {

    public double getSpeedInKmh() {
        return (speed == null) ? 0.0 : speed * 1.852;
    }
}
