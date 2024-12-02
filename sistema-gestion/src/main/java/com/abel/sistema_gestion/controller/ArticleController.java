package com.abel.sistema_gestion.controller;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.article.ArticleBasicSearchResponse;
import com.abel.sistema_gestion.dto.article.ArticleRequest;
import com.abel.sistema_gestion.dto.article.ArticleResponse;
import com.abel.sistema_gestion.serviceimpl.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createArticle(@Valid @RequestPart("article") ArticleRequest request,
                                                         @RequestPart("imageFile") MultipartFile imageFile) throws IOException {
        MessageResponse response = articleService.createArticle(request, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ArticleResponse>> getAllArticlesByUserId(@PathVariable Integer userId){
        List<ArticleResponse> responses = articleService.getAllArticlesByUserIdAndId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
    //TODO este se usa
    @GetMapping("/page/all")
    public ResponseEntity<Page<ArticleResponse>> getArticlesPageAll(
            @RequestParam Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(articleService.getArticlesByUserId(userId, pageable));
    }
    //TODO este se usa
    @GetMapping("/page/category")
    public ResponseEntity<Page<ArticleResponse>> getArticlesPageByCategory(
            @RequestParam Integer userId,
            @RequestParam Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(articleService.getArticlesByUserIdAndCategory(userId, categoryId, pageable));
    }
    //TODO este se usa
    @GetMapping("/page/search")
    public ResponseEntity<Page<ArticleResponse>> getArticlesPageByName(
            @RequestParam Integer userId,
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(articleService.getArticlesByUserIdAndNameLike(userId, name, pageable));
    }
    //TODO NUEVO DE PRUEBA
    @GetMapping("/page/search-2")
    public ResponseEntity<Map<String, Object>> getArticlesPageByFilters(
            @RequestParam Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(required = false) String search // Filtro de búsqueda por nombre
    ) {
        // Definir la paginación
        Pageable pageable = PageRequest.of(page, size);

        // Llama al servicio para obtener los artículos filtrados
        Page<ArticleResponse> articlesPage = articleService.getArticlesByUserIdAndSearch(userId, search, pageable);

        // Crear la respuesta para incluir la información de paginación
        Map<String, Object> response = new HashMap<>();
        response.put("articles", articlesPage.getContent());
        response.put("currentPage", articlesPage.getNumber());
        response.put("totalItems", articlesPage.getTotalElements());
        response.put("totalPages", articlesPage.getTotalPages());

        // Enlaces de navegación
        if (articlesPage.hasNext()) {
            response.put("nextPageUrl", "/articles/page/search-2?userId=" + userId + "&page=" + (page + 1) + "&size=" + size + (search != null ? "&search=" + search : ""));
        }
        if (articlesPage.hasPrevious()) {
            response.put("previousPageUrl", "/articles/page/search-2?userId=" + userId + "&page=" + (page - 1) + "&size=" + size + (search != null ? "&search=" + search : ""));
        }

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{userId}/{id}")
    public ResponseEntity<MessageResponse> deletedCategoryByUserIdAndId(@PathVariable Integer userId, @PathVariable Long id){
        MessageResponse response = articleService.deletedCategoryByUserIdAndId(userId, id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/detail/{id}/{userId}")
    public ResponseEntity<ArticleResponse> getByIdAndUserId(@PathVariable(required = true) Integer id,
                                                            @PathVariable(required = true) Integer userId){
        ArticleResponse response = articleService.getByIdAndUserId(id, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MessageResponse> updateArticle(@PathVariable Integer id, @RequestBody ArticleRequest request){
        MessageResponse response = articleService.updateArticle(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> getCountArticlesByUserId(@PathVariable Integer userId){
        return ResponseEntity.ok(articleService.getCountArticlesByUserId(userId));
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<ArticleBasicSearchResponse>> getSuggestedArticles(
            @RequestParam Integer userId,
            @RequestParam String search ){
        List<ArticleBasicSearchResponse> response = articleService.findArticlesByUserIdAndSearch(userId, search);
        return ResponseEntity.ok(response);
    }





}
