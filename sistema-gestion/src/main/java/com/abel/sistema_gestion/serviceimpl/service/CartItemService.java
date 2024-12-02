package com.abel.sistema_gestion.serviceimpl.service;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.cartItem.CartItemStockResponse;
import com.abel.sistema_gestion.dto.cartItem.CartItemTotalResponse;
import com.abel.sistema_gestion.dto.cartItem.UpdateQuantityRequest;

public interface CartItemService {
    CartItemStockResponse getCartItemStock(Integer articleId, Integer cartId);

    Boolean existArticleIdAndCartId(Integer articleId, Integer cartId);

    MessageResponse updateTotalAndQuantity(Integer cartItemId, UpdateQuantityRequest request);

    MessageResponse updateDecreaseQuantity(Integer cartItemId, UpdateQuantityRequest request);

    MessageResponse deletedProductCartItem(Integer cartItemId);
}
