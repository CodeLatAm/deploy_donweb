package com.abel.sistema_gestion.serviceimpl;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.cart.CartQuantityResponse;
import com.abel.sistema_gestion.dto.cart.CartResponse;
import com.abel.sistema_gestion.dto.cartItem.CartItemRequest;
import com.abel.sistema_gestion.dto.cartItem.CartItemTotalResponse;
import com.abel.sistema_gestion.entity.Article;
import com.abel.sistema_gestion.entity.Cart;
import com.abel.sistema_gestion.entity.CartItem;
import com.abel.sistema_gestion.exception.CartNotFoundException;
import com.abel.sistema_gestion.exception.InsufficientStockException;
import com.abel.sistema_gestion.mapper.CartMapper;
import com.abel.sistema_gestion.repository.CartItemRepository;
import com.abel.sistema_gestion.repository.CartRepository;
import com.abel.sistema_gestion.serviceimpl.service.ArticleService;
import com.abel.sistema_gestion.serviceimpl.service.CartService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
@Log4j2
@Service
public class CartServiceImpl implements CartService {

    private CartRepository cartRepository;
    private ArticleService articleService;
    private CartMapper cartMapper;
    private CartItemRepository cartItemRepository;

    public CartServiceImpl(CartRepository cartRepository, ArticleService articleService,
                           CartMapper cartMapper,
                           CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.articleService = articleService;
        this.cartMapper = cartMapper;
        this.cartItemRepository = cartItemRepository;
    }
    //TODO agregar cantidad al metodo como parametro
    @Transactional
    @Override
    public MessageResponse addArticleToCart(Integer cartId, Integer userId, Integer articleId, CartItemRequest request) {
        // Obtener el artículo
        Article article = articleService.getArticleByIdAndUserId(articleId, userId);
        // Verificar si hay suficiente stock disponible
        if(article.getQuantity() < request.getQuantity()){
            throw new InsufficientStockException("Stock insuficiente para el artículo con id: " + articleId);
        }
        // Obtener el carrito
        Cart cart = cartRepository.findById(cartId).orElseThrow(
                () -> new CartNotFoundException("Carrito no encontrado con id: " + cartId));

        // Verificar si el artículo ya está en el carrito
        boolean articleExistsInCart = false;
        for (CartItem cartItem: cart.getCartItems()) {
            if(cartItem.getArticle().getId().equals(articleId)) {
                // Si el artículo ya está en el carrito, incrementamos la cantidad
                int newQuantity = cartItem.getQuantity() + request.getQuantity();
                int articleStock = article.getQuantity();
                double newTotal = cartItem.getTotal() + request.getTotal();

                if (request.getQuantity() > articleStock) {
                    throw new InsufficientStockException("Stock insuficiente para aumentar la cantidad del artículo con id: " + articleId);
                }
                cartItem.setQuantity(newQuantity);
                cartItem.setTotal(newTotal);
                // Actualizar el stock del artículo
               // article.setQuantity(article.getQuantity() - request.getQuantity());
                //articleService.save(article); // Asegúrate de que este método guarda el artículo con el nuevo stock

                articleExistsInCart = true;
                break;  // Salgo del bucle una vez que se encuentra el artículo
            }
        }

        // Si el artículo no estaba en el carrito, lo agregamos como un nuevo CartItem
        if (!articleExistsInCart) {
            CartItem newItem = new CartItem();
                newItem.setArticle(article);
                newItem.setCart(cart);
                newItem.setQuantity(request.getQuantity());
                newItem.setTotal(request.getTotal());
                cart.getCartItems().add(newItem);
            // Actualizar el stock del artículo
            //article.setQuantity(article.getQuantity() - request.getQuantity());
            //articleService.save(article); // Asegúrate de que este método guarda el artículo con el nuevo stock

            // Actualizo el articulo descontando las cantidades
            //articleService.updateQuantityArticle(articleId, userId, request.getQuantity());
        }

        // Guardar el carrito actualizado
        cartRepository.save(cart);

        // Retornar respuesta
        return MessageResponse.builder()
                .message("Artículo guardado en ventas")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public CartResponse getCartByIdAndVendorId(Integer id, Integer vendorId) {
        Cart cart = cartRepository.findByIdAndVendorId(id, vendorId).orElseThrow(
                () -> new CartNotFoundException("Carrito no encontrado")
        );
        List<CartItem> cartItems = cart.getCartItems();
        Iterator<CartItem> iterator = cartItems.iterator();
        while (iterator.hasNext()) {
            CartItem cartItem = iterator.next();
            if (cartItem.getArticle() != null && cartItem.getArticle().getQuantity() == 0) {
                iterator.remove();
                cartItemRepository.delete(cartItem);
                log.info("CartItem borrado " + cartItem.getCart().getId());
            }
        }
        CartResponse response = cartMapper.mapToCart(cart);
        return response;
    }

    @Override
    public void deleteCartCartItem(Integer cartId, Integer cartItemId) {
        // Buscar el CartItem por cartItemId y cartId
        CartItem cartItem = cartItemRepository.findByIdAndCartId(cartItemId, cartId)
                .orElseThrow(() -> new CartNotFoundException("CartItem no encontrado"));

        // Eliminar el CartItem encontrado
        cartItemRepository.delete(cartItem);
    }

    @Override
    public CartQuantityResponse getCartQuantityFor(Integer cartId, Integer vendorId) {
        Cart cart = cartRepository.findByIdAndVendorId(cartId, vendorId).orElseThrow(
                () -> new CartNotFoundException("Carrito no encontrado")
        );
        int totalQuantity = cart.getCartItems().size();
        return CartQuantityResponse.builder()
                .quantity(totalQuantity)
                .build();
    }

    @Override
    public CartItemTotalResponse getCartItemTotal(Integer cartId, Integer vendorId) {
        Cart cart = cartRepository.findByIdAndVendorId(cartId, vendorId).orElseThrow(
                () -> new CartNotFoundException("Carrito no encontrado")
        );
        // Calcular la cantidad total de productos (sumar todas las cantidades)
        int totalQuantityForCartItem = cart.getCartItems().stream()
                .mapToInt(CartItem::getQuantity) // Obtener la cantidad de cada CartItem
                .sum();

        // Calcular el total monetario de los productos (sumar todos los totales de cada CartItem)
        double totalValueForCartItem = cart.getCartItems().stream()
                .mapToDouble(CartItem::getTotal) // Obtener el total de cada CartItem
                .sum();

        return CartItemTotalResponse.builder()
                .totalProductQuantity(totalQuantityForCartItem)
                .totalProductValue(totalValueForCartItem)
                .build();
    }

    @Override
    public Cart getCartBy(Integer cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart con ID " + cartId + " not found"));
    }

}
