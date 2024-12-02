package com.abel.sistema_gestion.mapper;

import com.abel.sistema_gestion.dto.article.ArticleBasicSearchResponse;
import com.abel.sistema_gestion.dto.article.ArticleRequest;
import com.abel.sistema_gestion.dto.article.ArticleResponse;

import com.abel.sistema_gestion.dto.category.CategoryResponse;
import com.abel.sistema_gestion.dto.image.ImageResponse;
import com.abel.sistema_gestion.dto.vendorSale.ArticleBasicResponse;
import com.abel.sistema_gestion.entity.Article;
import com.abel.sistema_gestion.entity.Category;
import com.abel.sistema_gestion.entity.Image;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArticleMapper {
    public Article mapTpArticleRequest(ArticleRequest request) {
        //Venta es el calculo de (price * profitPercentage)/100 + price = sale
        //double sale = request.getPrice() * request.getProfitPercentage() / 100 + request.getPrice();
        return Article.builder()
                //.sale(sale)
                .price(request.getPrice())
                .name(request.getName())
                //.profitPercentage(request.getProfitPercentage())
                .quantity(request.getQuantity())
                .revenue(request.getRevenue())
                .description(request.getDescription())
                .build();
    }

    public List<ArticleResponse> mapToArticleList(List<Article> articles) {
        return articles.stream().map(this::mapToArticle)
                .collect(Collectors.toList());
    }

    public ArticleResponse mapToArticle(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .userId(article.getUser().getId())
                .price(article.getPrice())
                .name(article.getName())
                .quantity(article.getQuantity())
                .revenue(article.getRevenue())
                .description(article.getDescription())
                .imageResponse(this.mapToImagesArticle(article.getImages()))
                .categoryResponse(this.mapToCategory(article.getCategory()))
                .build();
    }

    public CategoryResponse mapToCategory(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public List<ImageResponse> mapToImagesArticle(List<Image> images) {
        return images.stream().map(this::imageEntity)
                .collect(Collectors.toList());
    }

    public ImageResponse imageEntity(Image image) {
        return ImageResponse.builder()
                .contentType(image.getContentType())
                .data(image.getData())
                .id(image.getId())
                .name(image.getName())
                .build();
    }


    public ArticleBasicResponse mapToArticleBasic(Article article) {
        return ArticleBasicResponse.builder()
                .id(article.getId())
                .price(article.getPrice())
                .name(article.getName())
                .imageResponse(this.mapToImagesArticle(article.getImages()))
                .build();
    }

    public void mapToArticleUpdate(Article article, ArticleRequest request) {
        article.setQuantity(request.getQuantity());
        article.setDescription(request.getDescription());
        article.setName(request.getName());
        article.setPrice(request.getPrice());
        article.setRevenue(request.getRevenue());

    }

    public List<ArticleBasicSearchResponse> mapToArticleBasicSearch(List<Article> articles) {
        return articles.stream().map(
                this::mapToBasicArticle
        ).collect(Collectors.toList());
    }

    private ArticleBasicSearchResponse mapToBasicArticle(Article article) {
        return ArticleBasicSearchResponse.builder()
                .id(article.getId())
                .description(article.getDescription())
                .name(article.getName())
                .build();
    }
}
