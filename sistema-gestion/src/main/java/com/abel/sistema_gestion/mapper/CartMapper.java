package com.abel.sistema_gestion.mapper;

import com.abel.sistema_gestion.dto.cart.CartResponse;
import com.abel.sistema_gestion.dto.vendor.VendorCartResponse;
import com.abel.sistema_gestion.entity.Cart;
import com.abel.sistema_gestion.entity.Vendor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CartMapper {

    private final ArticleMapper articleMapper;
    private final CartItemMapper cartItemMapper;
    public CartResponse mapToCart(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .vendorCartResponse(this.mapToVendor(cart.getVendor()))
                //.articleResponseList(articleMapper.mapToArticleList(cart.getArticleList()))
                .cartItemResponses(cartItemMapper.mapToCartItem(cart.getCartItems()))
                .build();
    }

    private VendorCartResponse mapToVendor(Vendor vendor) {
        return VendorCartResponse.builder()
                .id(vendor.getId())
                .email(vendor.getEmail())
                .name(vendor.getName())
                .role(vendor.getRole())
                .vendorStatus(vendor.getVendorStatus().name())
                .surname(vendor.getSurname())
                .userId(vendor.getUserId())
                .build();
    }
}
