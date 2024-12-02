package com.abel.sistema_gestion.repository;

import com.abel.sistema_gestion.entity.VendorStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorStatusHistoryRepository extends JpaRepository<VendorStatusHistory, Integer> {
}
