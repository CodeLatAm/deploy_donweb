package com.abel.sistema_gestion.dto.article;

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
public class ArticleVendorSaleResponse {

    private Integer id;
    private String name;
    private Double price;
    private List<ImageResponse> imageResponse;
}
