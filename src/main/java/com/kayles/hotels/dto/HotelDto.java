package com.kayles.hotels.dto;

import com.kayles.hotels.dto.informationDto.AddressDto;
import com.kayles.hotels.dto.informationDto.ArrivalTimeDto;
import com.kayles.hotels.dto.informationDto.ContactsDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record HotelDto (
        @NotNull(message = "The id should not be empty")
        Long id,

        @NotBlank(message = "The name should not be empty")
        @Size(min = 2, max = 40, message = "The name must contain from 2 to 40 characters")
        String name,

        String description,

        @NotBlank(message = "The brand should not be empty")
        String brand,

        @Valid
        AddressDto address,

        @Valid
        ContactsDto contacts,

        @Valid
        ArrivalTimeDto arrivalTime,

        @NotEmpty(message = "Amenities list should not be empty")
        List<@NotBlank(message = "Each amenity must not be blank") String> amenities
) {
}
