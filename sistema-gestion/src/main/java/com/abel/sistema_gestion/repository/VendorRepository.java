package com.abel.sistema_gestion.repository;

import com.abel.sistema_gestion.entity.Vendor;
import com.abel.sistema_gestion.enums.VendorStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Integer>, JpaSpecificationExecutor<Vendor> {
    boolean existsByEmailAndUserId(String email, Integer userId);

    List<Vendor> findByUserId(Integer userId);

    boolean existsByUserId(Integer userId);

    Optional<Vendor> findByEmail(String username);

    boolean existsByEmail(String email);

    Optional<Vendor> findByUserIdAndId(Integer userId, Integer id);

    Optional<Vendor> findByIdAndUserId(Integer id, Integer userId);

    @Query("SELECT COUNT(v) FROM Vendor v WHERE v.userId = :userId")
    Integer getCountVendorsByUserId(@Param("userId") Integer userId);

    @Query("SELECT COUNT(DISTINCT c) FROM Vendor v JOIN v.clientList c WHERE v.userId = :userId")
    Integer countDistinctClientsByUserId(@Param("userId") Integer userId);

    List<Vendor> findAllByUserId(Integer userId);

    int countByUserIdAndPlan(Integer userId, String plan);

    List<Vendor> findAllByUserIdAndActivoTrue(Integer userId);

    boolean existsByEmailAndVendorStatusIn(String email, List<VendorStatus> statuses);


}
