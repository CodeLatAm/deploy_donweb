package com.abel.sistema_gestion.specification;

import com.abel.sistema_gestion.entity.Vendor;
import org.springframework.data.jpa.domain.Specification;

public class VendorSpecification {
    public static Specification<Vendor> byUserIdAndActivo(Integer userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("userId"), userId),
                criteriaBuilder.isTrue(root.get("activo")));
    }

    public static Specification<Vendor> filterByAttributes(String filter) {
        return (root, query, criteriaBuilder) -> {
            String likePattern = "%" + filter + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(root.get("name"), likePattern),
                    criteriaBuilder.like(root.get("email"), likePattern)
            );
        };
    }
}

