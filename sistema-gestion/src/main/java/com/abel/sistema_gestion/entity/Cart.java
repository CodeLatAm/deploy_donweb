package com.abel.sistema_gestion.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;


    // Lista de CartItems que representan artículos en el carrito junto con su cantidad
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    // Método para agregar artículos al carrito
    public void addArticle(Article article) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getArticle().equals(article)) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                return;
            }
        }
        CartItem newItem = new CartItem();
        newItem.setArticle(article);
        newItem.setCart(this);
        newItem.setQuantity(1);
        cartItems.add(newItem);
    }

}
