package com.kayles.hotels.dto.informationDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record ContactsDto(

        @NotBlank(message = "The phone should not be empty")
        @Pattern(regexp = "^\\+?[0-9]{10,15}$",
                message = "Invalid phone number format")
        String phone,

        @Email
        String email
) {
}
