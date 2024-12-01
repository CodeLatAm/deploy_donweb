package com.abel.sistema_gestion.repository;

import com.abel.sistema_gestion.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Integer>, JpaSpecificationExecutor<Zone> {
    //boolean existsByLocationAndUserId(String location, Integer userId);

    List<Zone> findByUserId(Integer userId);

    boolean existsByLocationAndUserIdAndProvince(String location, Integer userId, String province);

    boolean existsByLocationAndUserId(String location, Integer userId);
}
