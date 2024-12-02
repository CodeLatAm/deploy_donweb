package com.abel.sistema_gestion.serviceimpl.service;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.zone.ZoneRequest;
import com.abel.sistema_gestion.dto.zone.ZoneResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ZoneService {
    MessageResponse createZone(ZoneRequest request);

    List<ZoneResponse> getAllFindByUserId(Integer userId);

    void deletedZone(Integer zoneId);

    ZoneResponse updateZone(Integer zoneId, ZoneRequest request);

    ZoneResponse getZoneById(Integer zoneId);

    Page<ZoneResponse> getAllZonesByUserIdPage(Integer userId, Pageable pageable, String search);
}
