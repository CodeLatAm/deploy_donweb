package com.abel.sistema_gestion.controller;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.zone.ZoneRequest;
import com.abel.sistema_gestion.dto.zone.ZoneResponse;
import com.abel.sistema_gestion.serviceimpl.service.ZoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/zone")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class ZoneController {

    private final ZoneService zoneService;
    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createZone(@Valid @RequestBody ZoneRequest request){
        MessageResponse response = zoneService.createZone(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/all/{userId}")
    public ResponseEntity<List<ZoneResponse>> getAllZonesByUserId(@PathVariable Integer userId){
        List<ZoneResponse> responses = zoneService.getAllFindByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PreAuthorize("hasAnyAuthority('PROPIETARIO')")
    @GetMapping("/all/{userId}/search")
    public ResponseEntity<Page<ZoneResponse>> getAllZonesByUserIdPage(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<ZoneResponse> zoneResponses = zoneService.getAllZonesByUserIdPage(userId ,pageable, search);
        return ResponseEntity.ok(zoneResponses);
    }

    @PreAuthorize("hasAnyAuthority('PROPIETARIO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletedZone(@PathVariable Integer id){
        zoneService.deletedZone(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("hasAnyAuthority('PROPIETARIO')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ZoneResponse> updateZone(@PathVariable Integer id,@RequestBody ZoneRequest request){
        ZoneResponse response = zoneService.updateZone(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZoneResponse> getZoneById(@PathVariable Integer id){
        ZoneResponse response = zoneService.getZoneById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
