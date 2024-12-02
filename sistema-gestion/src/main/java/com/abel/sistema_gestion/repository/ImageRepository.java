package com.abel.sistema_gestion.repository;

import com.abel.sistema_gestion.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    void deleteByArticleId(Integer id);
}
