package com.kayles.hotels.controller;

import com.kayles.hotels.dto.HotelCreateDto;
import com.kayles.hotels.dto.HotelDto;
import com.kayles.hotels.dto.HotelShortDto;
import com.kayles.hotels.service.HotelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Validated
public class HotelControllerImpl implements HotelController {

    private final HotelService hotelService;

    @Override
    public ResponseEntity<Page<HotelShortDto>> getAllHotels(Pageable pageable) {
        return ResponseEntity.ok(hotelService.readAllHotels(pageable));
    }

    @Override
    public ResponseEntity<HotelDto> getHotelById(Long id) {
        return ResponseEntity.ok(hotelService.readHotelById(id));
    }

    @Override
    public ResponseEntity<List<HotelShortDto>> searchHotels(String name, String brand, String city,
                                                            String country, Set<String> amenities) {
        return ResponseEntity.ok(hotelService.searchHotels(name, brand, city, country, amenities));
    }

    @Override
    public ResponseEntity<HotelShortDto> createHotel(HotelCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.createHotel(dto));
    }

    @Override
    public ResponseEntity<HotelDto> addAmenities(Long id, Set<String> amenities) {
        return ResponseEntity.ok(hotelService.addAmenities(id, amenities));
    }

    @Override
    public ResponseEntity<Map<String, Long>> getHistogram(String param) {
        return ResponseEntity.ok(hotelService.getHistogram(param));
    }
}
