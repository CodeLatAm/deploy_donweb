package com.abel.sistema_gestion.serviceimpl;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.zone.ZoneRequest;
import com.abel.sistema_gestion.dto.zone.ZoneResponse;
import com.abel.sistema_gestion.entity.User;
import com.abel.sistema_gestion.entity.Zone;
import com.abel.sistema_gestion.exception.ZoneNotFoundException;
import com.abel.sistema_gestion.mapper.ZoneMapper;
import com.abel.sistema_gestion.repository.ZoneRepository;
import com.abel.sistema_gestion.serviceimpl.service.UserService;
import com.abel.sistema_gestion.serviceimpl.service.ZoneService;
import com.abel.sistema_gestion.specification.VendorSpecification;
import com.abel.sistema_gestion.specification.ZoneSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ZoneServiceImpl implements ZoneService {

    private ZoneRepository zoneRepository;
    private ZoneMapper zoneMapper;

    private UserService userService;

    public ZoneServiceImpl(ZoneRepository zoneRepository, ZoneMapper zoneMapper, UserService userService) {
        this.zoneRepository = zoneRepository;
        this.zoneMapper = zoneMapper;
        this.userService = userService;
    }

    @Transactional
    @Override
    public MessageResponse createZone(ZoneRequest request) {
        if(zoneRepository.existsByLocationAndUserIdAndProvince(request.getLocation(), request.getUserId(), request.getProvince())){
            throw new ZoneNotFoundException("Ya esta creada esta zona");
        }
        Zone zone = zoneMapper.mapToZoneRequest(request);
        zoneRepository.save(zone);
        return MessageResponse.builder()
                .message("Zona creada")
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @Override
    public List<ZoneResponse> getAllFindByUserId(Integer userId) {
        List<Zone> zoneList = zoneRepository.findByUserId(userId);
        List<ZoneResponse> responses = zoneMapper.mapToZoneResponseList(zoneList);
        return responses;
    }

    @Override
    public void deletedZone(Integer zoneId) {
        Zone zone = zoneRepository.findById(zoneId).orElseThrow(
                () -> {
                    throw  new ZoneNotFoundException("Zona no encontrada con id: " + zoneId);
                }
        );
        zoneRepository.delete(zone);
    }

    @Override
    public ZoneResponse updateZone(Integer zoneId, ZoneRequest request) {
        //Todo no dejar q se actualize por una localidad ya registrada
        /*if(zoneRepository.existsByLocationAndUserId(request.getLocation(), request.getUserId())){
            throw new ZoneNotFoundException("Ya esta creada esta zona");
        }*/
        Zone zone = zoneRepository.findById(zoneId).orElseThrow( () -> {
           throw new ZoneNotFoundException("Zona no encontrada con id: " + zoneId);
        });
        zoneMapper.mapToZoneUpdate(zone, request);
        zoneRepository.save(zone);
        return zoneMapper.mapToZoneResponse(zone);
    }

    @Override
    public ZoneResponse getZoneById(Integer zoneId) {
        Zone zone = zoneRepository.findById(zoneId).orElseThrow(() -> {
            throw new ZoneNotFoundException("Zona no encontrada con id: " + zoneId);
        });
        return zoneMapper.mapToZoneResponse(zone);
    }

    @Override
    public Page<ZoneResponse> getAllZonesByUserIdPage(Integer userId, Pageable pageable, String search) {
        User user = userService.getUserByUserId(userId);
        Specification<Zone> specification = Specification.where(ZoneSpecification.byUserId(userId))
                .and(ZoneSpecification.filterByAttributes(search));
        if(search != null && !search.isEmpty()) {
            specification = specification.and(ZoneSpecification.filterByAttributes(search));
        }
        Page<Zone> zones = zoneRepository.findAll(specification, pageable);
        return zoneMapper.mapToZonesPageResponse(zones);
    }
}
