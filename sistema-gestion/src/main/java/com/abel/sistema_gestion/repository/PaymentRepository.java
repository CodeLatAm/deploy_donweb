package com.abel.sistema_gestion.repository;

import com.abel.sistema_gestion.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findAllByUserIdOrderByDateCreatedDesc(Integer userId);

    @Query("SELECT YEAR(p.dateCreated) AS year, SUM(p.amount) AS total " +
            "FROM Payment p " +
            "WHERE p.status = 'APPROVED' " +
            "GROUP BY YEAR(p.dateCreated) " +
            "ORDER BY YEAR(p.dateCreated) DESC")
    List<Object[]> calculateEarningsByYear();
}
