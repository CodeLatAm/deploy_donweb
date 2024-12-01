package com.abel.sistema_gestion.dto.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRequest {

    @NotBlank(message = "El nombre es requerido")
    private String name;
    @NotNull(message = "El precio no puede ser nulo")
    @Min(value = 0,message = "El precio debe ser >= 0")
    private Double price;
    @NotNull(message = "La cantidad es obligatorio")
    @Min(value = 0, message = "La cantidad debe ser >= 0")
    private Integer quantity;
    @NotNull(message = "El userId es obligatorio")
    private Integer userId;
    @NotNull(message = "La ganacia es obligatorio")
    @Min(value = 0, message = "La cantidad debe ser >= 0")
    private Integer revenue;
    @NotNull(message = "El categoryId es obligatorio")
    private Long categoryId;

    private String description;
}
