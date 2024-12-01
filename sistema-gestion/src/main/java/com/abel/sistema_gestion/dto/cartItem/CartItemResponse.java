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
public class CartItemResponse {

    private Integer id;
    private ArticleResponse articleResponse;
    private Integer quantity;
    private Double total;

}
