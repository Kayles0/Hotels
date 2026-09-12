package com.kayles.hotels.service;

import com.kayles.hotels.dto.HotelCreateDto;
import com.kayles.hotels.dto.HotelDto;
import com.kayles.hotels.dto.HotelShortDto;
import com.kayles.hotels.entity.Hotel;
import com.kayles.hotels.exception.BaseException;
import com.kayles.hotels.exception.DuplicateException;
import com.kayles.hotels.exception.EntityNotFoundException;
import com.kayles.hotels.mapper.HotelMapper;
import com.kayles.hotels.repository.HotelRepository;
import com.kayles.hotels.util.HotelSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Transactional(readOnly = true)
    public Page<HotelShortDto> readAllHotels(Pageable pageable) {
        return hotelRepository.findAll(pageable).map(hotelMapper::toShortDto);
    }

    @Transactional(readOnly = true)
    public HotelDto readHotelById(Long id) {
        return hotelMapper.toDto(
                readById(id)
        );
    }

    @Transactional
    public HotelShortDto createHotel(HotelCreateDto dto) {
        if (hotelRepository.existsByName(dto.name())) {
            throw new DuplicateException("Hotel with name " + dto.name() + " already exists");
        }

        Hotel hotel = hotelMapper.toEntity(dto);
        hotel = hotelRepository.save(hotel);
        return hotelMapper.toShortDto(hotel);
    }

    @Transactional
    public HotelDto addAmenities(Long id, Set<String> amenities) {
        Hotel hotel = readById(id);
        hotel.getAmenities().addAll(amenities);

        hotelRepository.save(hotel);
        return hotelMapper.toDto(hotel);
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getHistogram(String param) {
        List<Object[]> results = switch (param.toLowerCase()) {
            case "brand" -> hotelRepository.getHistogramByBrand();
            case "city" -> hotelRepository.getHistogramByCity();
            case "country" -> hotelRepository.getHistogramByCountry();
            case "amenities" -> hotelRepository.getHistogramByAmenities();
            default -> throw new BaseException("Unknown parameter");
        };

        return results.stream()
                .collect(Collectors.toMap(
                        row -> String.valueOf(row[0]),
                        row -> (Long) row[1]
                ));
    }

    @Transactional(readOnly = true)
    public List<HotelShortDto> searchHotels(String name, String brand, String city, String country, Set<String> amenities) {
        Specification<Hotel> spec = HotelSpecification.search(name, brand, city, country, amenities);

        return hotelRepository.findAll(spec).stream()
                .map(hotelMapper::toShortDto)
                .toList();
    }

    private Hotel readById(Long id) {
        return hotelRepository
                .findById(id)
                .orElseThrow(() ->  new EntityNotFoundException("Hotel with id " + id + " not found"));
    }
}
