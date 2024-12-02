package com.abel.sistema_gestion.mapper;

import com.abel.sistema_gestion.dto.cartItem.CartItemResponse;
import com.abel.sistema_gestion.dto.cartItem.CartItemStockResponse;
import com.abel.sistema_gestion.dto.vendorSale.CartItemBasicResponse;
import com.abel.sistema_gestion.entity.CartItem;
import com.abel.sistema_gestion.entity.CartItemVendorSale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartItemMapper {

    private final ArticleMapper articleMapper;

    public List<CartItemResponse> mapToCartItem(List<CartItem> cartItems) {
        return cartItems.stream().map(
                this::mapToCartItemResponse
        ).collect(Collectors.toList());
    }

    private CartItemResponse mapToCartItemResponse(CartItem cartItem) {
        return CartItemResponse.builder()
                .id(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .total(cartItem.getTotal())
                .articleResponse(articleMapper.mapToArticle(cartItem.getArticle()))
                .build();
    }

    public CartItemStockResponse mapToCartItemEntity(CartItem cartItem) {
        return CartItemStockResponse.builder()
                .id(cartItem.getId())
                .cartId(cartItem.getCart().getId())
                .articleId(cartItem.getArticle().getId())
                .total(cartItem.getTotal())
                .quantity(cartItem.getQuantity())
                .build();
    }

    public List<CartItemResponse> mapToCartItemVendorSales(List<CartItemVendorSale> cartItemVendorSales) {

        return cartItemVendorSales.stream()
                .map(this::mapToCartItemVendorSalesResponse).collect(Collectors.toList());
    }

    private CartItemResponse mapToCartItemVendorSalesResponse(CartItemVendorSale cartItemVendorSale) {
        return CartItemResponse.builder()
                .total(cartItemVendorSale.getTotal())
                .id(cartItemVendorSale.getId())
                .quantity(cartItemVendorSale.getQuantity())
                .articleResponse(articleMapper.mapToArticle(cartItemVendorSale.getArticle()))
                .build();
    }

    public List<CartItemBasicResponse> mapToCartItemBasicResponse(List<CartItemVendorSale> cartItemVendorSales) {
        return cartItemVendorSales.stream().map(this::mapToCartItemBasic).collect(Collectors.toList());
    }

    private CartItemBasicResponse mapToCartItemBasic(CartItemVendorSale cart) {
        return CartItemBasicResponse.builder()
                .id(cart.getId())
                .total(cart.getTotal())
                .quantity(cart.getQuantity())
                .articleResponse(articleMapper.mapToArticleBasic(cart.getArticle()))
                .build();
    }
}
