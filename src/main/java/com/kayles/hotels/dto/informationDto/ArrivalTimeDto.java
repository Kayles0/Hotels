package com.kayles.hotels.dto.informationDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record ArrivalTimeDto(

        @NotNull(message = "Check-in time is required")
        @JsonFormat(pattern = "HH:mm")
        LocalTime checkIn,

        @JsonFormat(pattern = "HH:mm")
        LocalTime checkOut
) {
}
