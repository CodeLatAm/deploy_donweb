package com.abel.sistema_gestion.serviceimpl;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.cartItem.CartItemResponse;
import com.abel.sistema_gestion.dto.cartItem.CartItemStockResponse;
import com.abel.sistema_gestion.dto.cartItem.CartItemTotalResponse;
import com.abel.sistema_gestion.dto.cartItem.UpdateQuantityRequest;
import com.abel.sistema_gestion.entity.Article;
import com.abel.sistema_gestion.entity.Cart;
import com.abel.sistema_gestion.entity.CartItem;
import com.abel.sistema_gestion.exception.ArticleNotFountException;
import com.abel.sistema_gestion.exception.CartItemNotFoundException;
import com.abel.sistema_gestion.exception.CartNotFoundException;
import com.abel.sistema_gestion.mapper.CartItemMapper;
import com.abel.sistema_gestion.repository.ArticleRepository;
import com.abel.sistema_gestion.repository.CartItemRepository;
import com.abel.sistema_gestion.repository.CartRepository;
import com.abel.sistema_gestion.serviceimpl.service.CartItemService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl implements CartItemService {
    private CartItemRepository cartItemRepository;
    private CartItemMapper cartItemMapper;
    private CartRepository cartRepository;
    private ArticleRepository articleRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, CartItemMapper cartItemMapper, CartRepository cartRepository, ArticleRepository articleRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartItemMapper = cartItemMapper;
        this.cartRepository = cartRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public CartItemStockResponse getCartItemStock(Integer articleId, Integer cartId) {
        // Obtener las entidades Cart y Article usando los IDs
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart no encontrado"));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFountException("Article no encontrado"));

        // Buscar el CartItem usando las entidades
        CartItem cartItem = cartItemRepository.findByArticleAndCart(article, cart)
                .orElseThrow(() -> new CartItemNotFoundException("CartItem no encontrado"));

        return cartItemMapper.mapToCartItemEntity(cartItem);
    }

    @Override
    public Boolean existArticleIdAndCartId(Integer articleId, Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart no encontrado"));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFountException("Article no encontrado"));
        // Verificar si el carrito contiene el artículo
        return cart.getCartItems().stream()
                .anyMatch(cartItem -> cartItem.getArticle().getId().equals(articleId));
    }

    @Override
    public MessageResponse updateTotalAndQuantity(Integer cartItemId, UpdateQuantityRequest request) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("CartItem no encontrado"));
        Double newTotal = cartItem.getTotal() + (request.getQuantity() * cartItem.getArticle().getPrice());
        cartItem.setTotal(newTotal);
        cartItem.setQuantity(request.getQuantity() + cartItem.getQuantity());

        //cartItem.updateQuantityAndTotal(request.getQuantity(), newTotal);
        cartItemRepository.save(cartItem);
        return MessageResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("CartItem Actualizado")
                .build();
    }

    @Override
    public MessageResponse updateDecreaseQuantity(Integer cartItemId, UpdateQuantityRequest request) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("CartItem no encontrado"));

        if (cartItem.getQuantity() <= request.getQuantity()) {
            throw new IllegalArgumentException("La cantidad no puede ser menor a 1.");
        }
        Double newTotal = cartItem.getTotal() - (request.getQuantity() * cartItem.getArticle().getPrice());
        cartItem.setTotal(newTotal);
        cartItem.setQuantity(cartItem.getQuantity() - request.getQuantity());

        cartItemRepository.save(cartItem);
        return MessageResponse.builder().httpStatus(HttpStatus.OK)
                .message("CartItem Actualizado")
                .build();
    }

    @Override
    public MessageResponse deletedProductCartItem(Integer cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("CartItem no encontrado"));
        cartItemRepository.delete(cartItem);
        return MessageResponse.builder().httpStatus(HttpStatus.OK)
                .message("Producto eliminado")
                .build();
    }
}
