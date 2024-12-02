package com.abel.sistema_gestion.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    private Integer quantity;

    private Double total;

    public void updateQuantityAndTotal(Integer quantity, Double total){
        this.quantity = this.quantity + quantity;
        this.total = this.total + total;
    }
}
