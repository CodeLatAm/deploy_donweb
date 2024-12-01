package com.abel.sistema_gestion.repository;

import com.abel.sistema_gestion.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    void deleteAllByUserId(Integer id);
}
