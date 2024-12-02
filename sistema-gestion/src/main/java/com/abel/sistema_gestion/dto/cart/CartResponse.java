package com.abel.sistema_gestion.dto.cart;

import com.abel.sistema_gestion.dto.article.ArticleResponse;
import com.abel.sistema_gestion.dto.cartItem.CartItemResponse;
import com.abel.sistema_gestion.dto.vendor.VendorCartResponse;
import com.abel.sistema_gestion.dto.vendor.VendorResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {

    private Integer id;
    private VendorCartResponse vendorCartResponse;
    private List<CartItemResponse> cartItemResponses;
}
