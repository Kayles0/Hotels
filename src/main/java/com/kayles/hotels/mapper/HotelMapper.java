package com.kayles.hotels.mapper;

import com.kayles.hotels.dto.HotelCreateDto;
import com.kayles.hotels.dto.HotelDto;
import com.kayles.hotels.dto.HotelShortDto;
import com.kayles.hotels.entity.Hotel;
import com.kayles.hotels.entity.hotelInformation.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    HotelMapper INSTANCE = Mappers.getMapper(HotelMapper.class);

    HotelDto toDto(Hotel hotel);

    Hotel toEntity(HotelDto dto);

    List<HotelDto> toDtoList(List<Hotel> hotels);

    @Mapping(target = "address", source = "address", qualifiedByName = "formatAddress")
    @Mapping(target = "phone", source = "contacts.phone")
    HotelShortDto toShortDto(Hotel hotel);

    Hotel toEntity(HotelCreateDto dto);

    @Named("formatAddress")
    default String formatAddress(Address address) {
        if (address == null) return "";

        return String.format("%s %s, %s, %s, %s",
                address.getHouseNumber() != null ? address.getHouseNumber() : "",
                address.getStreet() != null ? address.getStreet() : "",
                address.getCity() != null ? address.getCity() : "",
                address.getPostCode() != null ? address.getPostCode() : "",
                address.getCountry() != null ? address.getCountry() : ""
        ).replaceAll("\\s+", " ").trim();
    }
}
