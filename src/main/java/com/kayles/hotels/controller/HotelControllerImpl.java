package com.kayles.hotels.controller;

import com.kayles.hotels.dto.HotelCreateDto;
import com.kayles.hotels.dto.HotelDto;
import com.kayles.hotels.dto.HotelShortDto;
import com.kayles.hotels.service.HotelService;
import com.kayles.hotels.service.HotelServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
public class HotelControllerImpl implements HotelController {

    private final HotelServiceImpl hotelService;

    /**
     * 1) GET /property-view/hotels
     * Получение списка всех отелей с их краткой информацией (с пагинацией)
     */
    @GetMapping("/hotels")
    public ResponseEntity<Page<HotelShortDto>> getAllHotels(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(hotelService.readAllHotels(pageable));
    }

    /**
     * 2) GET /property-view/hotels/{id}
     * Получение расширенной информации по конкретному отелю
     */
    @GetMapping("/hotels/{id}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.readHotelById(id));
    }

    /**
     * 3) GET /property-view/search
     * Поиск отелей по параметрам: name, brand, city, country, amenities
     * Пример: /property-view/search?city=minsk&amenities=wifi&amenities=pool
     */
    @GetMapping("/search")
    public ResponseEntity<List<HotelShortDto>> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Set<String> amenities) {
        return ResponseEntity.ok(hotelService.searchHotels(name, brand, city, country, amenities));
    }

    /**
     * 4) POST /property-view/hotels
     * Создание нового отеля
     */
    @PostMapping("/hotels")
    public ResponseEntity<HotelShortDto> createHotel(@Valid @RequestBody HotelCreateDto dto) {
        HotelShortDto created = hotelService.createHotel(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * 5) POST /property-view/hotels/{id}/amenities
     * Добавление списка amenities к отелю
     */
    @PostMapping("/hotels/{id}/amenities")
    public ResponseEntity<HotelDto> addAmenities(
            @PathVariable Long id,
            @RequestBody Set<String> amenities) {
        return ResponseEntity.ok(hotelService.addAmenities(id, amenities));
    }

    /**
     * 6) GET /property-view/histogram/{param}
     * Получение количества отелей, сгруппированных по указанному параметру
     * Параметр: brand, city, country, amenities
     */
    @GetMapping("/histogram/{param}")
    public ResponseEntity<Map<String, Long>> getHistogram(@PathVariable String param) {
        return ResponseEntity.ok(hotelService.getHistogram(param));
    }
}
