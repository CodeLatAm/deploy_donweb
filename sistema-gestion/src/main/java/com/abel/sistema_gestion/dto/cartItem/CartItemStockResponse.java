package com.abel.sistema_gestion.dto.cartItem;

import com.abel.sistema_gestion.dto.article.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemStockResponse {
    private Integer id;
    private Integer cartId;
    private Integer articleId;
    private Integer quantity;
    private Double total;
}
