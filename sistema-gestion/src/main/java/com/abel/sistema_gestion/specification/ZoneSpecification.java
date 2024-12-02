package com.abel.sistema_gestion.specification;

import com.abel.sistema_gestion.entity.Zone;
import org.springframework.data.jpa.domain.Specification;

public class ZoneSpecification {
    public static Specification<Zone> byUserId(Integer userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userId"), userId);
    }

    public static Specification<Zone> filterByAttributes(String search) {
        return (root, query, criteriaBuilder) -> {
            String likePattern = "%" + search + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(root.get("location"), likePattern),
                    criteriaBuilder.like(root.get("province"), likePattern),
                    criteriaBuilder.like(root.get("cp"), likePattern)
            );
        };
    }
}
