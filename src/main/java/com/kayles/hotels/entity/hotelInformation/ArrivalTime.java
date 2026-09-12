package com.kayles.hotels.entity.hotelInformation;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArrivalTime {
    private LocalTime checkIn;
    private LocalTime checkOut;
}
