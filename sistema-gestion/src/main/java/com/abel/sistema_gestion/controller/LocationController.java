package com.abel.sistema_gestion.controller;

import com.abel.sistema_gestion.dto.Location.LocationResponse;
import com.abel.sistema_gestion.serviceimpl.LocationServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class LocationController {

    private final LocationServiceImp locationService;

    @GetMapping("/localidades")
    public LocationResponse getLocalidades(@RequestParam String nombre) {
        return locationService.getLocationsByName(nombre);
    }
}
