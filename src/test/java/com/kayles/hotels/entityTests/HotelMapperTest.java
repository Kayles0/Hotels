package com.kayles.hotels.entityTests;

import com.kayles.hotels.dto.HotelDto;
import com.kayles.hotels.dto.informationDto.AddressDto;
import com.kayles.hotels.dto.informationDto.ArrivalTimeDto;
import com.kayles.hotels.dto.informationDto.ContactsDto;
import com.kayles.hotels.entity.Hotel;
import com.kayles.hotels.mapper.HotelMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class HotelMapperTest {

    private final HotelMapper hotelMapper = HotelMapper.INSTANCE;

    @Test
    void shouldMapDtoToEntity() {
        AddressDto addressDto = new AddressDto(
                10L,
                "Nemiga St",
                "Minsk",
                "Belarus",
                "220030"
        );

        ContactsDto contactsDto = new ContactsDto(
                "+375291234567",
                "test@mail.com"
        );

        ArrivalTimeDto arrivalDto = new ArrivalTimeDto(
                LocalTime.of(14, 0),
                LocalTime.of(12, 0)
        );

        HotelDto dto = new HotelDto(
                null,
                "Radisson",
                "Luxury hotel",
                "Radisson Blu",
                addressDto,
                contactsDto,
                arrivalDto,
                Set.of("WiFi", "Pool")
        );

        Hotel entity = hotelMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals("Radisson", entity.getName());

        assertNotNull(entity.getAddress());
        assertEquals(10L, entity.getAddress().getHouseNumber());
        assertEquals("Nemiga St", entity.getAddress().getStreet());
        assertEquals("Minsk", entity.getAddress().getCity());

        assertNotNull(entity.getContacts());
        assertEquals("+375291234567", entity.getContacts().getPhone());
        assertEquals("test@mail.com", entity.getContacts().getEmail());

        assertNotNull(entity.getArrivalTime());
        assertEquals(LocalTime.of(14, 0), entity.getArrivalTime().getCheckIn());

        assertEquals(2, entity.getAmenities().size());
        assertTrue(entity.getAmenities().contains("WiFi"));
    }

}
