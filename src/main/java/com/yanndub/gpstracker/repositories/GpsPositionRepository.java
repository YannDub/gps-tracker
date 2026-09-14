package com.yanndub.gpstracker.repositories;

import com.yanndub.gpstracker.entities.GpsPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GpsPositionRepository extends JpaRepository<GpsPosition, Integer> {

    @Query(value = """
            SELECT CASE WHEN ST_DWithin(
                ST_SetSRID(ST_MakePoint(:currentLon, :currentLat), 4326)::geography,
                ST_SetSRID(ST_MakePoint(:centerLon, :centerLat), 4326)::geography,
                :radiusInMeters
            ) THEN false ELSE true END
            """, nativeQuery = true)
    boolean isOutsideGeofence(
            @Param("currentLat") double currentLat,
            @Param("currentLon") double currentLon,
            @Param("centerLat") double centerLat,
            @Param("centerLon") double centerLon,
            @Param("radiusInMeters") double radiusInMeters
    );
}
