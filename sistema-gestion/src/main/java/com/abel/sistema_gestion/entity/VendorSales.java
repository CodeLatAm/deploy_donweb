package com.abel.sistema_gestion.entity;

import com.abel.sistema_gestion.enums.SaleStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vendor_sales")
public class VendorSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate date;
    @ManyToOne()
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;
    @ManyToOne()
    @JoinColumn(name = "client_id")
    private Client client;
    private String shippingArea;
    private String invoiceNumber;
    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;
    //TODO Se debe terminar pasar el total de la venta actualmente se maneja en el front esta parte
   // DecimalFormat decimalFormat = new DecimalFormat("#,###.00"); para el response
   // String formattedTotal = decimalFormat.format(total);
    private String paymentType;
    private Double total;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
   // @JoinColumn(name = "vendor_sales_id")  // Establece el campo de unión para enlazar con VendorSales
    private List<CartItemVendorSale> cartItemVendorSales;


}
