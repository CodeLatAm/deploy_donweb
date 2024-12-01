package com.abel.sistema_gestion.repository;

import com.abel.sistema_gestion.entity.Client;
import com.abel.sistema_gestion.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer>, JpaSpecificationExecutor<Client> {
    //List<Client> findByUserId(Integer userId);

    boolean existsByVendorIdAndEmail(Integer vendorId, String email);

    List<Client> findByVendorId(Integer vendorId);

    // Consulta para buscar clientes por vendorId y coincidencia en cualquier atributo
    @Query("SELECT c FROM Client c WHERE c.vendor.id = :vendorId AND (" +
            "LOWER(c.name) LIKE LOWER(CONCAT(:prefix, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT(:prefix, '%')) OR " +
            "LOWER(c.address) LIKE LOWER(CONCAT(:prefix, '%')) OR " +
            "LOWER(c.phone) LIKE LOWER(CONCAT(:prefix, '%')) OR " +
            "LOWER(c.cp) LIKE LOWER(CONCAT(:prefix, '%')) OR " +
            "LOWER(c.betweenStreets) LIKE LOWER(CONCAT(:prefix, '%')) OR " +
            "LOWER(c.location) LIKE LOWER(CONCAT(:prefix, '%')) OR " +
            "LOWER(c.dni) LIKE LOWER(CONCAT(:prefix, '%')) OR " +
            "LOWER(c.province) LIKE LOWER(CONCAT(:prefix, '%'))) ")
    List<Client> searchByVendorIdAndAttribute(@Param("vendorId") Integer vendorId,
                                              @Param("prefix") String prefix);

    boolean existsByVendorIdAndDni(Integer vendorId, String dni);

    @Query("SELECT COUNT(c) FROM Client c WHERE c.vendor.id = :vendorId")
    Long countClientsByVendorId(@Param("vendorId") Integer vendorId);

    @Query("SELECT COUNT(DISTINCT c) FROM Client c WHERE c.vendor.userId = :userId")
    Long countDistinctClientsByUserId(@Param("userId") Integer userId);

    List<Client> findAllByVendorAndActivoTrue(Vendor vendor);

}
