package com.yanndub.gpstracker.services;

import com.yanndub.gpstracker.repositories.GpsPositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class DataRetentionService {
    private static final Logger LOG = LoggerFactory.getLogger(DataRetentionService.class);

    private final GpsPositionRepository repository;
    private final int retentionDays;

    public DataRetentionService(
            final GpsPositionRepository repository,
            @Value("${app.retention.days}") final int retentionDays) {
        this.repository = repository;
        this.retentionDays = retentionDays;
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void purgeOldPosition() {
        final var cutOffDate = Instant.now().minus(retentionDays, ChronoUnit.DAYS);

        LOG.info("Debut de la purge des positions GPS antérieues au {} (rétention : {} jours)", cutOffDate, retentionDays);
        final var deletedCount = repository.deleteOldNonAlertPositions(cutOffDate);
        LOG.info("Purge terminée : {} positions obsolètes supprimées", deletedCount);
    }
}
