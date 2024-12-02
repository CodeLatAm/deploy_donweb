package com.abel.sistema_gestion.repository;

import com.abel.sistema_gestion.entity.FeePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeePriceRepository extends JpaRepository<FeePrice, Long> {
}
