package com.abel.sistema_gestion.serviceimpl.service;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.cart.CartQuantityResponse;
import com.abel.sistema_gestion.dto.cart.CartResponse;
import com.abel.sistema_gestion.dto.cartItem.CartItemRequest;
import com.abel.sistema_gestion.dto.cartItem.CartItemTotalResponse;
import com.abel.sistema_gestion.entity.Cart;

import java.util.Optional;

public interface CartService {
    MessageResponse addArticleToCart(Integer articleId, Integer userId, Integer cartId, CartItemRequest request);

    CartResponse getCartByIdAndVendorId(Integer id, Integer vendorId);


    void deleteCartCartItem(Integer cartId, Integer cartItemId);

    CartQuantityResponse getCartQuantityFor(Integer cartId, Integer vendorId);

    CartItemTotalResponse getCartItemTotal(Integer cartId, Integer vendorId);

    Cart getCartBy(Integer cartId);
}
