package com.kayles.hotels.service;

import com.kayles.hotels.dto.HotelCreateDto;
import com.kayles.hotels.dto.HotelDto;
import com.kayles.hotels.dto.HotelShortDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public interface HotelService {

    Page<HotelShortDto> readAllHotels(Pageable pageable);

    HotelDto readHotelById(Long id);

    HotelShortDto createHotel(HotelCreateDto dto);

    HotelDto addAmenities(Long id, Set<String> amenities);

    Map<String, Long> getHistogram(String param);

    Page<HotelShortDto> searchHotels(String name, String brand, String city, String country, Set<String> amenities, Pageable pageable);
}
