package com.kayles.hotels.mapper;

import com.kayles.hotels.dto.informationDto.ContactsDto;
import com.kayles.hotels.entity.hotelInformation.Contacts;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactsMapper {
    ContactsDto toDto(Contacts contacts);
    Contacts toEntity(ContactsDto dto);
}
