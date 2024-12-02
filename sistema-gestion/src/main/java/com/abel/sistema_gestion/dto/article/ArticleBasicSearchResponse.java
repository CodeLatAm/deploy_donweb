package com.abel.sistema_gestion.dto.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleBasicSearchResponse {
    private Integer id;
    private String name;
    private String description;
}
