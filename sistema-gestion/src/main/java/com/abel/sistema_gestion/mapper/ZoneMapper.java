package com.abel.sistema_gestion.mapper;

import com.abel.sistema_gestion.dto.zone.ZoneRequest;
import com.abel.sistema_gestion.dto.zone.ZoneResponse;
import com.abel.sistema_gestion.entity.Zone;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ZoneMapper {
    public Zone mapToZoneRequest(ZoneRequest request) {
        return Zone.builder()
                .userId(request.getUserId())
                .cost(request.getCost())
                .location(request.getLocation())
                .cp(request.getCp())
                .province(request.getProvince())
                .build();
    }

    public List<ZoneResponse> mapToZoneResponseList(List<Zone> zoneList) {
        return zoneList.stream()
                .map(this::mapToZoneResponse)
                .collect(Collectors.toList());
    }

    public ZoneResponse mapToZoneResponse(Zone zone) {
        return ZoneResponse.builder()
                .id(zone.getId())
                .cost(zone.getCost())
                .cp(zone.getCp())
                .location(zone.getLocation())
                .userId(zone.getUserId())
                .province(zone.getProvince())
                .build();
    }

    public void mapToZoneUpdate(Zone zone, ZoneRequest request) {
        zone.setUserId(request.getUserId());
        zone.setCp(request.getCp());
        zone.setLocation(request.getLocation());
        zone.setCost(request.getCost());
        zone.setProvince(request.getProvince());

    }

    public Page<ZoneResponse> mapToZonesPageResponse(Page<Zone> zones) {
        return zones.map(this::mapToZoneResponse);
    }
}
