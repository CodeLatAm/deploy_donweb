package com.abel.sistema_gestion.specification;

import com.abel.sistema_gestion.entity.Client;
import org.springframework.data.jpa.domain.Specification;

public class ClientSpecification {
    public static Specification<Client> byVendorId(Integer vendorId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("vendor").get("id"), vendorId),
                criteriaBuilder.isTrue(root.get("activo")));
    }

    public static Specification<Client> filterByAttributes(String filter) {
        return (root, query, criteriaBuilder) -> {
            String likePattern = "%" + filter + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(root.get("name"), likePattern),
                    criteriaBuilder.like(root.get("email"), likePattern),
                    criteriaBuilder.like(root.get("dni"), likePattern)
                    // Agrega más filtros según los atributos de Client
            );
        };
    }
}
