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
@Table(name = "vendor_sale_item")
public class CartItemVendorSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    private Integer quantity;

    private Double total;

    public void discountQuantityArticle(){
        article.setQuantity(article.getQuantity() - quantity);
    }
}
