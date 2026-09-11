package com.kayles.hotels.mapper;

import com.kayles.hotels.dto.HotelDto;
import com.kayles.hotels.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    HotelMapper INSTANCE = Mappers.getMapper(HotelMapper.class);

    HotelDto toDto(Hotel hotel);

    Hotel toEntity(HotelDto dto);

    List<HotelDto> toDtoList(List<Hotel> hotels);
}
