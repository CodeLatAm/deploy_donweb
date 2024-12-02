package com.abel.sistema_gestion.serviceimpl;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.article.ArticleBasicSearchResponse;
import com.abel.sistema_gestion.dto.article.ArticleRequest;
import com.abel.sistema_gestion.dto.article.ArticleResponse;
import com.abel.sistema_gestion.entity.Article;
import com.abel.sistema_gestion.entity.Category;
import com.abel.sistema_gestion.entity.Image;
import com.abel.sistema_gestion.entity.User;
import com.abel.sistema_gestion.exception.ArticleAlreadyExistException;
import com.abel.sistema_gestion.exception.ArticleNotFountException;
import com.abel.sistema_gestion.exception.UserNotFoundException;
import com.abel.sistema_gestion.mapper.ArticleMapper;
import com.abel.sistema_gestion.repository.ArticleRepository;
import com.abel.sistema_gestion.serviceimpl.service.ArticleService;
import com.abel.sistema_gestion.serviceimpl.service.CategoryService;
import com.abel.sistema_gestion.serviceimpl.service.UserService;
import lombok.extern.log4j.Log4j2;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class ArticleServiceImpl implements ArticleService {

    private ArticleRepository articleRepository;
    private ArticleMapper articleMapper;
    private UserService userService;

    private CategoryService categoryService;

    // Se usa injection por constructor porque si se usa loom-book hay conflict con los test
    public ArticleServiceImpl(ArticleRepository articleRepository, ArticleMapper articleMapper,
                              UserService userService, CategoryService categoryService) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Transactional
    @Override
    public MessageResponse createArticle(ArticleRequest request, MultipartFile imageFile) throws IOException {
        if(articleRepository.existsByNameAndUserId(request.getName(), request.getUserId())){
            throw new ArticleAlreadyExistException("El articulo ya existe");
        }
        User user = userService.getUserByUserId(request.getUserId());
        Category category = categoryService.findById(request.getCategoryId());
        Article article = articleMapper.mapTpArticleRequest(request);
        article.setCategory(category);
        article.setUser(user);
        if(imageFile != null && !imageFile.isEmpty()){
            Image image = Image.builder()
                    .data(imageFile.getBytes())
                    .contentType(imageFile.getContentType())
                    .name(imageFile.getName())
                    .article(article)
                    .build();
            article.setImages(Collections.singletonList(image));
        }
        articleRepository.save(article);

        return new MessageResponse("Articulo creado", HttpStatus.CREATED);
    }

    @Override
    public List<ArticleResponse> getAllArticlesByUserIdAndId(Integer userId) {
        List<Article> articles = articleRepository.findByUserId(userId);
        List<ArticleResponse> responses = articleMapper.mapToArticleList(articles);
        return responses;
    }

    @Override
    public Page<ArticleResponse> getArticlesByUserId(Integer userId, Pageable pageable) {
        return articleRepository.findByUserId(userId, pageable)
                .map(this::convertToArticleResponse);

    }

    @Override
    public Page<ArticleResponse> getArticlesByUserIdAndCategory(Integer userId, Long categoryId, Pageable pageable) {
        return articleRepository.findByUserIdAndCategoryId(userId, categoryId, pageable)
                .map(this::convertToArticleResponse);
    }


    private ArticleResponse convertToArticleResponse(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .price(article.getPrice())
                .categoryResponse(articleMapper.mapToCategory(article.getCategory()))
                .quantity(article.getQuantity())
                .userId(article.getUser().getId())
                .name(article.getName())
                .revenue(article.getRevenue())
                .description(article.getDescription())
                .imageResponse(articleMapper.mapToImagesArticle(article.getImages()))
                .build();
    }

    @Override
    public MessageResponse deletedCategoryByUserIdAndId(Integer userId, Long id) {
        Optional<Article> article = articleRepository.findByUserIdAndId(userId, id);
        if(article.isEmpty()){
            throw new ArticleNotFountException("Articulo not fount");
        }
        articleRepository.delete(article.get());
        return new MessageResponse("Articulo borrado", HttpStatus.OK);
    }

    @Override
    public ArticleResponse getByIdAndUserId(Integer id, Integer userId) {
        Optional<Article> article = articleRepository.findByIdAndUserId(id, userId);
        if(article.isEmpty()){
            throw new ArticleNotFountException("Articulo no encontrado");
        }
        ArticleResponse response = articleMapper.mapToArticle(article.get());
        return response;
    }

    @Override
    public Article getArticleByIdAndUserId(Integer articleId, Integer userId) {
        Article article = articleRepository.findByIdAndUserId(articleId,userId).orElseThrow(
                () ->  new ArticleNotFountException("Articulo no encontrado"));

        return article;
    }

    @Override
    public void updateQuantityArticle(Integer articleId, Integer userId, Integer quantity) {
        log.info("Articulo sin actualizar = " );
        Article article = articleRepository.findByIdAndUserId(articleId,userId).orElseThrow(
                () ->  new ArticleNotFountException("Articulo no encontrado"));
        log.info("Articulo sin actualizar = "  + article.getQuantity());
        Integer quantityUpdate = article.getQuantity() - quantity;
        article.setQuantity(quantityUpdate);
        log.info("Articulo actualizado cantidad = " + article.getQuantity());
        articleRepository.save(article);
    }

    @Transactional
    @Override
    public void save(Article article) {
        articleRepository.save(article);
    }

    @Override
    public Article getArticleById(Integer articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new ArticleNotFountException("ARticulo no encontrado con id: " + articleId)
        );
        return article;
    }

    @Transactional
    @Override
    public MessageResponse updateArticle(Integer id, ArticleRequest request) {
        Article article = articleRepository.findById(id).orElseThrow(
                () -> new ArticleNotFountException("Articulo no encontrado con id: " + id));
        Category category = categoryService.findById(request.getCategoryId());
        article.setCategory(category);
        articleMapper.mapToArticleUpdate(article, request);
        articleRepository.save(article);
        return new MessageResponse("Articulo actualizado", HttpStatus.OK);
    }

    @Override
    public Long getCountArticlesByUserId(Integer userId) {
        /*if(!articleRepository.existsByUserId(userId)){
            throw new UserNotFoundException("Usuario no encontrado con id " + userId);
        }*/
        return articleRepository.countArticlesByUserId(userId);
    }

    @Override
    public Optional<Article> findByNameAndUserId(String productName, Integer userId) {
        return articleRepository.findByNameAndUserId(productName,userId);
    }

    @Override
    public Page<ArticleResponse> getArticlesByUserIdAndNameLike(Integer userId, String name, Pageable pageable) {
        return articleRepository.findByUserIdAndNameContainingIgnoreCase(userId, name, pageable)
                .map(this::convertToArticleResponse);
    }

    //PAGINACION NUEVO


    @Override
    public Page<ArticleResponse> getArticlesByUserIdAndSearch(Integer userId, String search, Pageable pageable) {
        // Si hay un valor en el filtro de búsqueda (search)
        if (search != null && !search.isEmpty()) {
            return articleRepository.findByUserIdAndNameContaining(userId, search, pageable)
                    .map(this::convertToArticleResponse);
        }
        // Si no hay filtro, devolver todos los artículos del usuario
        return articleRepository.findByUserId(userId, pageable)
                .map(this::convertToArticleResponse);
    }

    @Override
    public List<ArticleBasicSearchResponse> findArticlesByUserIdAndSearch(Integer userId, String search) {
        User user = userService.getUserByUserId(userId);
        List<Article> articles = articleRepository.findByUserAndNameContainingIgnoreCase(user, search);
        return articleMapper.mapToArticleBasicSearch(articles);
    }
}
