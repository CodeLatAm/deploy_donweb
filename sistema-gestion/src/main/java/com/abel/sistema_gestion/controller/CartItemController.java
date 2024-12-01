package com.abel.sistema_gestion.controller;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.cartItem.CartItemStockResponse;
import com.abel.sistema_gestion.dto.cartItem.UpdateQuantityRequest;
import com.abel.sistema_gestion.serviceimpl.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartItems")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CartItemController {

    private final CartItemService cartItemService;
    @GetMapping("/articles/{articleId}/carts/{cartId}")
    public ResponseEntity<CartItemStockResponse> getCartItemStock(@PathVariable Integer articleId, @PathVariable Integer cartId){
        CartItemStockResponse response = cartItemService.getCartItemStock(articleId, cartId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exist/{articleId}/carts/{cartId}")
   public ResponseEntity<Boolean> existArticleIdAndCartId (@PathVariable Integer articleId, @PathVariable Integer cartId){
        Boolean existArticle = cartItemService.existArticleIdAndCartId(articleId,cartId);
        return ResponseEntity.ok(existArticle);
   }
   @PutMapping("/{cartItemId}/quantity")
   public ResponseEntity<MessageResponse> updateTotalAndQuantity(@PathVariable Integer cartItemId,
                                                                 @RequestBody UpdateQuantityRequest request){
        MessageResponse response = cartItemService.updateTotalAndQuantity(cartItemId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{cartItemId}/decrease")
    public ResponseEntity<MessageResponse> updateDecreaseQuantity(@PathVariable Integer cartItemId,
                                                                  @RequestBody UpdateQuantityRequest request){
        MessageResponse response = cartItemService.updateDecreaseQuantity(cartItemId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleted/{cartItemId}")
    public ResponseEntity<MessageResponse> deletedProductCartItem(@PathVariable Integer cartItemId){
        MessageResponse response = cartItemService.deletedProductCartItem(cartItemId);
        return ResponseEntity.ok(response);
    }




}
