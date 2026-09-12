package com.kayles.hotels.entity.hotelInformation;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private Long houseNumber;
    private String street;
    private String city;
    private String country;
    private String postCode;
}
