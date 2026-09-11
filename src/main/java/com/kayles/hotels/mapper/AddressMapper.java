package com.kayles.hotels.mapper;

import com.kayles.hotels.dto.informationDto.AddressDto;
import com.kayles.hotels.entity.hotelInformation.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDto toDto(Address address);
    Address toEntity(AddressDto dto);
}
