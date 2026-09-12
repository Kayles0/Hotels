package com.kayles.hotels.dto.informationDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AddressDto (

        @NotNull(message = "The house number should not be empty")
        Long houseNumber,

        @NotBlank(message = "The street should not be empty")
        @Size(min = 2, max = 40, message = "The street must contain from 2 to 40 characters")
        String street,

        @NotBlank(message = "The city should not be empty")
        @Size(min = 2, max = 40, message = "The city must contain from 2 to 40 characters")
        String city,

        @NotBlank(message = "The country should not be empty")
        @Size(min = 2, max = 40, message = "The country must contain from 2 to 40 characters")
        String country,

        @NotBlank(message = "The post code should not be empty")
        @Size(min = 3, max = 10, message = "The post code must contain from 3 to 10 characters")
        String postCode
) {
}
