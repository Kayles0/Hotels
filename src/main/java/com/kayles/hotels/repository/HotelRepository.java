package com.kayles.hotels.repository;

import com.kayles.hotels.dto.HotelShortDto;
import com.kayles.hotels.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {

    Page<HotelShortDto> readAllHotels(Pageable pageable);

    Optional<Hotel> readById(Long id);

    boolean existsByName(String name);

    @Query("SELECT h.brand, COUNT(h) FROM Hotel h WHERE h.brand IS NOT NULL GROUP BY h.brand")
    List<Object[]> getHistogramByBrand();

    @Query("SELECT h.address.city, COUNT(h) FROM Hotel h WHERE h.address.city IS NOT NULL GROUP BY h.address.city")
    List<Object[]> getHistogramByCity();

    @Query("SELECT h.address.country, COUNT(h) FROM Hotel h WHERE h.address.country IS NOT NULL GROUP BY h.address.country")
    List<Object[]> getHistogramByCountry();

    @Query("SELECT a, COUNT(h) FROM Hotel h JOIN h.amenities a WHERE a IS NOT NULL GROUP BY a")
    List<Object[]> getHistogramByAmenities();
}
