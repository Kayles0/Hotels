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
    public ResponseEntity<Page<HotelShortDto>> getAllHotels(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(hotelService.readAllHotels(pageable));
    }

    @Override
    public ResponseEntity<HotelDto> getHotelById(@NotNull(message = "Hotel ID must not be null") Long id) {
        return ResponseEntity.ok(hotelService.readHotelById(id));
    }

    @Override
    public ResponseEntity<List<HotelShortDto>> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Set<String> amenities) {
        return ResponseEntity.ok(hotelService.searchHotels(name, brand, city, country, amenities));
    }

    @Override
    public ResponseEntity<HotelShortDto> createHotel(@Valid @RequestBody  @NotNull(message = "Request body must not be null") HotelCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.createHotel(dto));
    }

    @Override
    public ResponseEntity<HotelDto> addAmenities(@NotNull(message = "Hotel ID must not be null") Long id,
                                                 @RequestBody @NotNull(message = "Amenities list must not be null") Set<String> amenities) {
        return ResponseEntity.ok(hotelService.addAmenities(id, amenities));
    }

    @Override
    public ResponseEntity<Map<String, Long>> getHistogram(@NotNull(message = "Parameter must not be null") String param) {
        return ResponseEntity.ok(hotelService.getHistogram(param));
    }
}
