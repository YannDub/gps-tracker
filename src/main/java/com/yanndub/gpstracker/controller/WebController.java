package com.yanndub.gpstracker.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class WebController {

    @Value("${app.geofencing.parking-latitude}")
    private double defaultLat;

    @Value("${app.geofencing.parking-longitude}")
    private double defaultLng;

    @Value("${app.geofencing.max-radius-meters}")
    private double defaultRadius;

    @GetMapping("/")
    public String index(final Model model) {
        model.addAllAttributes(Map.of(
                "defaultLat", defaultLat,
                "defaultLng", defaultLng,
                "defaultRadius", defaultRadius
        ));
        return "index";
    }
}
