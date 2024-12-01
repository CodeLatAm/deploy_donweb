package com.abel.sistema_gestion.repository;

import com.abel.sistema_gestion.entity.Article;
import com.abel.sistema_gestion.entity.Cart;
import com.abel.sistema_gestion.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    Optional<CartItem> findByIdAndCartId(Integer cartItemId, Integer cartId);

    Optional<CartItem> findByArticleAndCart(Article article, Cart cart);
}
