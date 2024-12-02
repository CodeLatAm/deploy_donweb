package com.abel.sistema_gestion.dto.vendorSale;

import com.abel.sistema_gestion.dto.article.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemBasicResponse {

    private Integer id;
    private ArticleBasicResponse articleResponse;
    private Integer quantity;
    private Double total;
}
