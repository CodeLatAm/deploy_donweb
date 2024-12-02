package com.abel.sistema_gestion.dto.article;

import com.abel.sistema_gestion.dto.category.CategoryResponse;
import com.abel.sistema_gestion.dto.image.ImageResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponse {

    private Integer id;

    private String name;

    private Double price;

    private Integer quantity;

    private Integer userId;

    private Integer revenue;

    private String description;

    private List<ImageResponse> imageResponse;

    private CategoryResponse categoryResponse;

}
