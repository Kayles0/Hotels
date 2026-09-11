package com.kayles.hotels.util;

import com.kayles.hotels.entity.Hotel;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HotelSpecification {

    public static Specification<Hotel> search(
            String name, String brand, String city, String country, Set<String> amenities) {

        return (Root<Hotel> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (brand != null && !brand.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("brand")), "%" + brand.toLowerCase() + "%"));
            }

            if (city != null && !city.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("address").get("city")), "%" + city.toLowerCase() + "%"));
            }

            if (country != null && !country.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("address").get("country")), "%" + country.toLowerCase() + "%"));
            }

            if (amenities != null && !amenities.isEmpty()) {
                query.distinct(true);
                Join<Hotel, String> amenitiesJoin = root.join("amenities", JoinType.LEFT);
                predicates.add(amenitiesJoin.in(amenities));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
