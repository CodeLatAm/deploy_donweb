package com.abel.sistema_gestion.repository;

import com.abel.sistema_gestion.dto.article.ArticleResponse;
import com.abel.sistema_gestion.entity.Article;
import com.abel.sistema_gestion.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    List<Article> findByUserId(Integer userId);

    boolean existsByNameAndUserId(String name, Integer userId);
    Page<Article> findByUserId(Integer userId, Pageable pageable);

    Page<Article> findByUserIdAndCategoryId(Integer userId, Long categoryId, Pageable pageable);

    Page<Article> findByUserIdAndNameContainingIgnoreCase(Integer userId, String name, Pageable pageable);

    Optional<Article> findByUserIdAndId(Integer userId, Long id);

    Optional<Article> findByIdAndUserId(Integer id, Integer userId);

    boolean existsByUserId(Integer userId);

    @Query("SELECT COUNT(a) FROM Article a WHERE a.user.id = :userId")
    Long countArticlesByUserId(@Param("userId") Integer userId);

    Optional<Article> findByNameAndUserId(String productName, Integer userId);

    // Buscar por usuario y nombre
    Page<Article> findByUserIdAndNameContaining(Integer userId, String name, Pageable pageable);

    List<Article> findByUserAndNameContainingIgnoreCase(User user, String search);
}
