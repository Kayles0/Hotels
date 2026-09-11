package com.kayles.hotels.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record HotelShortDto(

        @NotNull(message = "The id should not be empty")
        Long id,

        @NotBlank(message = "The name should not be empty")
        @Size(min = 2, max = 40, message = "The name must contain from 2 to 40 characters")
        String name,

        String description,

        @NotBlank(message = "The address should not be empty")
        @Size(min = 2, max = 40, message = "The address must contain from 2 to 40 characters")
        String address,

        @NotBlank(message = "The phone should not be empty")
        @Pattern(regexp = "^\\+?[0-9]{10,15}$",
                message = "Invalid phone number format")
        String phone
) {
}
