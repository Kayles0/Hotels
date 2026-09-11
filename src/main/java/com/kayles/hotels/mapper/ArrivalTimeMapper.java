package com.kayles.hotels.mapper;

import com.kayles.hotels.dto.informationDto.ArrivalTimeDto;
import com.kayles.hotels.entity.hotelInformation.ArrivalTime;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArrivalTimeMapper {
    ArrivalTimeDto toDto(ArrivalTime arrivalTime);
    ArrivalTime toEntity(ArrivalTimeDto dto);
}
