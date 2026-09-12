package com.kayles.hotels.util;

import com.kayles.hotels.entity.Hotel;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import java.util.Set;

public class HotelSpecification {

    public static Specification<Hotel> search(
            String name, String brand, String city, String country, Set<String> amenities) {

        return Specification.where(hasName(name))
                .and(hasBrand(brand))
                .and(hasCity(city))
                .and(hasCountry(country))
                .and(hasAllAmenities(amenities));
    }

    private static Specification<Hotel> hasName(String name) {
        return (root, query, cb) ->
                (name != null && !name.isBlank()) ? cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%") : null;
    }

    private static Specification<Hotel> hasBrand(String brand) {
        return (root, query, cb) ->
                (brand != null && !brand.isBlank()) ? cb.like(cb.lower(root.get("brand")), "%" + brand.toLowerCase() + "%") : null;
    }

    private static Specification<Hotel> hasCity(String city) {
        return (root, query, cb) ->
                (city != null && !city.isBlank()) ? cb.like(cb.lower(root.get("address").get("city")), "%" + city.toLowerCase() + "%") : null;
    }

    private static Specification<Hotel> hasCountry(String country) {
        return (root, query, cb) ->
                (country != null && !country.isBlank()) ? cb.like(cb.lower(root.get("address").get("country")), "%" + country.toLowerCase() + "%") : null;
    }

    private static Specification<Hotel> hasAllAmenities(Set<String> amenities) {
        return (root, query, cb) -> {
            if (amenities == null || amenities.isEmpty()) {
                return null;
            }

            query.distinct(true);

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Hotel> subroot = subquery.from(Hotel.class);
            Join<Hotel, String> subJoin = subroot.join("amenities");

            subquery.select(cb.count(subJoin))
                    .where(
                            cb.equal(subroot.get("id"), root.get("id")),
                            subJoin.in(amenities)
                    );

            return cb.equal(subquery, (long) amenities.size());
        };
    }
}