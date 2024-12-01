package com.abel.sistema_gestion.repository;

import com.abel.sistema_gestion.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByIdAndVendorId(Integer id, Integer vendorId);
}
