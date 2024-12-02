package com.abel.sistema_gestion.controller;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.cart.CartQuantityRequest;
import com.abel.sistema_gestion.dto.cart.CartQuantityResponse;
import com.abel.sistema_gestion.dto.cart.CartResponse;
import com.abel.sistema_gestion.dto.cartItem.CartItemRequest;
import com.abel.sistema_gestion.dto.cartItem.CartItemTotalResponse;
import com.abel.sistema_gestion.serviceimpl.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;
    //TODO TERMINAR
    @PutMapping("/{cartId}/users/{userId}/articles/{articleId}/add")
    public ResponseEntity<MessageResponse> addArticleToCart(@PathVariable Integer cartId,
                                                            @PathVariable Integer userId,
                                                            @PathVariable Integer articleId,
                                                            @Valid @RequestBody CartItemRequest request) {
        MessageResponse response = cartService.addArticleToCart(cartId, userId, articleId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/{vendorId}")
    public ResponseEntity<CartResponse> getCartByIdAndVendorId(@PathVariable Integer id, @PathVariable Integer vendorId){
        CartResponse response = cartService.getCartByIdAndVendorId(id, vendorId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cartId}/cartItems/{cartItemId}")
    public ResponseEntity<Void> deleteCartCartItem (@PathVariable Integer cartId, @PathVariable Integer cartItemId){
        cartService.deleteCartCartItem(cartId, cartItemId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/quantity/{cartId}/vendor/{vendorId}")
    public ResponseEntity<CartQuantityResponse> getCartQuantityFor(@PathVariable Integer cartId,
                                                                   @PathVariable Integer vendorId){
        CartQuantityResponse response = cartService.getCartQuantityFor(cartId, vendorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cartId}/vendor/{vendorId}")
    public ResponseEntity<CartItemTotalResponse> getCartItemTotal(@PathVariable Integer cartId,
                                                                  @PathVariable Integer vendorId){
        CartItemTotalResponse response= cartService.getCartItemTotal(cartId, vendorId);
        return ResponseEntity.ok(response);
    }


}
