package com.yanndub.gpstracker.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class TelegramNotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramNotificationService.class);

    private final RestClient restClient;
    private final String botToken;
    private final String chatId;

    public TelegramNotificationService(
            final RestClient.Builder restClientBuilder,
            @Value("${telegram.bot.token}") final String botToken,
            @Value("${telegram.chat.id}") final String chatId) {
        this.restClient = restClientBuilder.build();
        this.chatId = chatId;
        this.botToken = botToken;
    }

    public void sendAlert(String deviceId, double latitude, double longitude, Double speed, Double battery) {
        final var googleMapLink = String.format("https://maps.google.com/?q=%f,%f", latitude, longitude);

        final var message = String.format("""
                🚨 <b>ALERTE VOL / DEPLACEMENT DECTECTÉ !</b>
                
                🚗 <b>Véhicule :</b> %s
                📍 <b>Position :</b> %.6f, %.6f
                ⚡ <b>Vitesse :</b> %.1f km/h
                🔋 <b>Batterie :</b> %.0f%%

                🗺️ <a href="%s">Voir la position sur Google Maps</a>
                """, deviceId, latitude, longitude,
                speed == null ? 0.0 : speed,
                battery == null ? 0.0 : battery,
                googleMapLink);

        final var url = String.format("https://api.telegram.org/bot%s/sendMessage", botToken);

        try {
            restClient.post()
                    .uri(url)
                    .body(Map.of(
                            "chat_id", chatId,
                            "text", message,
                            "parse_mode", "HTML",
                            "disable_web_page_preview", false
                    ))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'envoie de la notification Telegram", e);
        }
    }
}
