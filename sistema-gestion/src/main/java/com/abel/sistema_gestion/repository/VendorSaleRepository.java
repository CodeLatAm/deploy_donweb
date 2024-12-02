package com.abel.sistema_gestion.repository;

import com.abel.sistema_gestion.entity.Vendor;
import com.abel.sistema_gestion.entity.VendorSales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VendorSaleRepository extends JpaRepository<VendorSales, Integer> {

    //@Query("SELECT YEAR(vs.date) AS year, COUNT(vs) AS count FROM VendorSales vs GROUP BY YEAR(vs.date)")
    //List<Object[]> countSalesByYear();
    @Query("SELECT YEAR(vs.date) AS year, COUNT(vs) AS count FROM VendorSales vs WHERE vs.vendor.id = :vendorId GROUP BY YEAR(vs.date)")
    List<Object[]> countSalesByYearAndVendorId(@Param("vendorId") Integer vendorId);


    @Query("SELECT v.shippingArea, COUNT(v) FROM VendorSales v WHERE v.vendor.id = :vendorId GROUP BY v.shippingArea")
    List<Object[]> findSalesCountByShippingArea(@Param("vendorId") Integer vendorId);

    // Obtener el número de clientes únicos que han realizado compras


    // Obtener el número total de ventas realizadas
    @Query("SELECT COUNT(vs) FROM VendorSales vs WHERE vs.vendor.id = :vendorId")
    Long countSalesByVendorId(@Param("vendorId") Integer vendorId);

    boolean existsByVendorId(Integer vendorId);

    @Query("SELECT YEAR(vs.date) AS year, COUNT(vs) AS salesCount " +
            "FROM VendorSales vs " +
            "JOIN vs.vendor v " +
            "WHERE v.userId = :userId " +
            "GROUP BY YEAR(vs.date)")
    List<Object[]> countSalesByYearAndUserId(@Param("userId") Integer userId);

    @Query("SELECT vs.shippingArea AS area, COUNT(vs) AS salesCount " +
            "FROM VendorSales vs " +
            "JOIN vs.vendor v " +
            "WHERE v.userId = :userId " +
            "GROUP BY vs.shippingArea")
    List<Object[]> countSalesByShippingAreaAndUserId(@Param("userId") Integer userId);

    @Query("SELECT COUNT(vs) FROM VendorSales vs WHERE vs.vendor.userId = :userId")
    Long countSalesByUserId(@Param("userId") Integer userId);

    // Query que permite buscar por algún campo (puedes personalizar los campos de búsqueda)
    @Query("SELECT vs FROM VendorSales vs WHERE vs.vendor.id = :vendorId " +
            "AND (:search IS NULL OR " +
            "LOWER(vs.client.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(vs.client.dni) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(vs.invoiceNumber) LIKE LOWER(CONCAT('%', :search, '%')))" +
            "ORDER BY vs.date DESC")
    Page<VendorSales> findByVendorIdAndSearch(Integer vendorId, String search, Pageable pageable);

    // Buscar ventas por año con paginación
    // Filtrar por año y Vendor
    @Query("SELECT vs FROM VendorSales vs WHERE vs.vendor = :vendor AND YEAR(vs.date) = :year")
    Page<VendorSales> findByVendorAndYear(@Param("vendor") Vendor vendor, @Param("year") Integer year, Pageable pageable);

    // Filtrar por rango de fechas y Vendor
    Page<VendorSales> findByVendorAndDateBetween(Vendor vendor, LocalDate startDate, LocalDate endDate, Pageable pageable);

    // Devolver todas las ventas de un vendedor
    Page<VendorSales> findByVendor(Vendor vendor, Pageable pageable);

    @Query("SELECT vs.paymentType, COUNT(vs) AS totalSales " +
            "FROM VendorSales vs " +
            "WHERE vs.vendor.userId = :userId " +
            "GROUP BY vs.paymentType")
    List<Object[]> findSalesCountByPaymentType(@Param("userId") Integer userId);

}
