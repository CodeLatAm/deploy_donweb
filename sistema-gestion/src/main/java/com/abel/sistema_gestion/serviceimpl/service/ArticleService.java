package com.abel.sistema_gestion.serviceimpl.service;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.article.ArticleBasicSearchResponse;
import com.abel.sistema_gestion.dto.article.ArticleRequest;
import com.abel.sistema_gestion.dto.article.ArticleResponse;
import com.abel.sistema_gestion.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ArticleService {
    MessageResponse createArticle(ArticleRequest request, MultipartFile imageFile) throws IOException;

    List<ArticleResponse> getAllArticlesByUserIdAndId(Integer userId);
    Page<ArticleResponse> getArticlesByUserId(Integer userId, Pageable pageable);

    Page<ArticleResponse> getArticlesByUserIdAndCategory(Integer userId, Long categoryId, Pageable pageable);

    Page<ArticleResponse>getArticlesByUserIdAndNameLike(Integer userId, String name, Pageable pageable);

    MessageResponse deletedCategoryByUserIdAndId(Integer userId, Long id);

    ArticleResponse getByIdAndUserId(Integer id, Integer userId);

    Article getArticleByIdAndUserId(Integer articleId, Integer userId);

    void updateQuantityArticle(Integer articleId, Integer userId, Integer quantity);

    void save(Article article);

    Article getArticleById(Integer articleId);


    MessageResponse updateArticle(Integer id, ArticleRequest request);

    Long getCountArticlesByUserId(Integer userId);


    Optional<Article> findByNameAndUserId(String productName, Integer userId);


    Page<ArticleResponse> getArticlesByUserIdAndSearch(Integer userId, String search, Pageable pageable);

    List<ArticleBasicSearchResponse> findArticlesByUserIdAndSearch(Integer userId, String search);
}
