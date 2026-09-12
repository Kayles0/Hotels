package com.kayles.hotels.serviceTest;

import com.kayles.hotels.dto.HotelCreateDto;
import com.kayles.hotels.dto.HotelDto;
import com.kayles.hotels.dto.HotelShortDto;
import com.kayles.hotels.entity.Hotel;
import com.kayles.hotels.exception.BaseException;
import com.kayles.hotels.exception.DuplicateException;
import com.kayles.hotels.exception.EntityNotFoundException;
import com.kayles.hotels.mapper.HotelMapper;
import com.kayles.hotels.repository.HotelRepository;
import com.kayles.hotels.service.HotelServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    private static final Long HOTEL_ID = 1L;
    private static final String HOTEL_NAME = "Grand Hotel";

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelServiceImpl hotelService;

    private Hotel hotel;
    private HotelDto hotelDto;
    private HotelShortDto hotelShortDto;
    private HotelCreateDto createDto;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setId(HOTEL_ID);
        hotel.setName(HOTEL_NAME);
        hotel.setAmenities(new HashSet<>());

        hotelDto = new HotelDto(HOTEL_ID, HOTEL_NAME, null, null, null,
                null, null, Set.of());
        hotelShortDto = new HotelShortDto(HOTEL_ID, HOTEL_NAME, null, null, null);

        createDto = HotelCreateDto.builder()
                .name(HOTEL_NAME)
                .build();
    }

    @Nested
    @DisplayName("readAllHotels")
    class ReadAllHotels {

        @Test
        void shouldReturnPageFromRepository() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<HotelShortDto> expected = new PageImpl<>(List.of(hotelShortDto));
            when(hotelRepository.findAll(pageable).map(hotelMapper::toShortDto)).thenReturn(expected);

            Page<HotelShortDto> actual = hotelService.readAllHotels(pageable);

            assertThat(actual).isSameAs(expected);
            verify(hotelRepository).findAll(pageable);
        }

        @Test
        void shouldReturnEmptyPage() {
            Pageable pageable = PageRequest.of(0, 10);
            when(hotelRepository.findAll(pageable)).thenReturn(Page.empty());

            assertThat(hotelService.readAllHotels(pageable)).isEmpty();
        }
    }

    @Nested
    @DisplayName("readHotelById")
    class ReadHotelById {

        @Test
        void shouldReturnHotelDto() {
            when(hotelRepository.findById(HOTEL_ID)).thenReturn(Optional.of(hotel));
            when(hotelMapper.toDto(hotel)).thenReturn(hotelDto);

            HotelDto actual = hotelService.readHotelById(HOTEL_ID);

            assertThat(actual).isEqualTo(hotelDto);
            verify(hotelRepository).findById(HOTEL_ID);
            verify(hotelMapper).toDto(hotel);
        }

        @Test
        void shouldThrowWhenNotFound() {
            when(hotelRepository.findById(HOTEL_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> hotelService.readHotelById(HOTEL_ID))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining(String.valueOf(HOTEL_ID));

            verify(hotelMapper, never()).toDto(any());
        }
    }

    @Nested
    @DisplayName("createHotel")
    class CreateHotel {

        @Test
        void shouldCreateHotel() {
            when(hotelRepository.existsByName(HOTEL_NAME)).thenReturn(false);
            when(hotelMapper.toEntity(createDto)).thenReturn(hotel);
            when(hotelRepository.save(hotel)).thenReturn(hotel);
            when(hotelMapper.toShortDto(hotel)).thenReturn(hotelShortDto);

            HotelShortDto actual = hotelService.createHotel(createDto);

            assertThat(actual).isEqualTo(hotelShortDto);
            verify(hotelRepository).save(hotel);
        }

        @Test
        void shouldThrowWhenNameDuplicated() {
            when(hotelRepository.existsByName(HOTEL_NAME)).thenReturn(true);

            assertThatThrownBy(() -> hotelService.createHotel(createDto))
                    .isInstanceOf(DuplicateException.class)
                    .hasMessageContaining(HOTEL_NAME);

            verify(hotelRepository, never()).save(any());
            verify(hotelMapper, never()).toEntity((HotelDto) any());
        }

        @Test
        void shouldReturnSavedEntityNotInputEntity() {
            Hotel saved = new Hotel();
            saved.setId(99L);
            saved.setName(HOTEL_NAME);
            saved.setAmenities(new HashSet<>());

            when(hotelRepository.existsByName(HOTEL_NAME)).thenReturn(false);
            when(hotelMapper.toEntity(createDto)).thenReturn(hotel);
            when(hotelRepository.save(hotel)).thenReturn(saved);
            when(hotelMapper.toShortDto(saved)).thenReturn(hotelShortDto);

            hotelService.createHotel(createDto);

            ArgumentCaptor<Hotel> captor = ArgumentCaptor.forClass(Hotel.class);
            verify(hotelMapper).toShortDto(captor.capture());
            assertThat(captor.getValue().getId()).isEqualTo(99L);
        }
    }

    @Nested
    @DisplayName("addAmenities")
    class AddAmenities {

        @Test
        void shouldAddAmenitiesToExistingSet() {
            Set<String> existing = new HashSet<>(Set.of("wifi"));
            hotel.setAmenities(existing);
            when(hotelRepository.findById(HOTEL_ID)).thenReturn(Optional.of(hotel));
            when(hotelRepository.save(hotel)).thenReturn(hotel);
            when(hotelMapper.toDto(hotel)).thenReturn(hotelDto);

            hotelService.addAmenities(HOTEL_ID, Set.of("pool", "spa"));

            assertThat(hotel.getAmenities())
                    .containsExactlyInAnyOrder("wifi", "pool", "spa");
            verify(hotelRepository).save(hotel);
        }

        @Test
        void shouldNotDuplicateExistingAmenity() {
            Set<String> existing = new HashSet<>(Set.of("wifi"));
            hotel.setAmenities(existing);
            when(hotelRepository.findById(HOTEL_ID)).thenReturn(Optional.of(hotel));
            when(hotelRepository.save(hotel)).thenReturn(hotel);
            when(hotelMapper.toDto(hotel)).thenReturn(hotelDto);

            hotelService.addAmenities(HOTEL_ID, Set.of("wifi"));

            assertThat(hotel.getAmenities()).containsExactly("wifi");
        }

        @Test
        void shouldReturnMappedDto() {
            when(hotelRepository.findById(HOTEL_ID)).thenReturn(Optional.of(hotel));
            when(hotelRepository.save(hotel)).thenReturn(hotel);
            when(hotelMapper.toDto(hotel)).thenReturn(hotelDto);

            assertThat(hotelService.addAmenities(HOTEL_ID, Set.of("gym")))
                    .isEqualTo(hotelDto);
        }

        @Test
        void shouldThrowWhenHotelNotFound() {
            when(hotelRepository.findById(HOTEL_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> hotelService.addAmenities(HOTEL_ID, Set.of("gym")))
                    .isInstanceOf(EntityNotFoundException.class);

            verify(hotelRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("getHistogram")
    class GetHistogram {

        @Test
        void shouldReturnBrandHistogram() {
            when(hotelRepository.getHistogramByBrand())
                    .thenReturn(List.of(new Object[]{"Hilton", 3L}, new Object[]{"Marriott", 5L}));

            Map<String, Long> actual = hotelService.getHistogram("brand");

            assertThat(actual).containsExactlyInAnyOrderEntriesOf(
                    Map.of("Hilton", 3L, "Marriott", 5L));
            verify(hotelRepository).getHistogramByBrand();
        }

        @Test
        void shouldReturnCityHistogram() {
            when(hotelRepository.getHistogramByCity())
                    .thenReturn(List.<Object[]>of(new Object[]{"Minsk", 2L}));

            assertThat(hotelService.getHistogram("city"))
                    .containsEntry("Minsk", 2L);
            verify(hotelRepository).getHistogramByCity();
        }

        @Test
        void shouldReturnCountryHistogram() {
            when(hotelRepository.getHistogramByCountry())
                    .thenReturn(List.<Object[]>of(new Object[]{"Belarus", 7L}));

            assertThat(hotelService.getHistogram("country"))
                    .containsEntry("Belarus", 7L);
            verify(hotelRepository).getHistogramByCountry();
        }

        @Test
        void shouldReturnAmenitiesHistogram() {
            when(hotelRepository.getHistogramByAmenities())
                    .thenReturn(List.<Object[]>of(new Object[]{"wifi", 10L}));

            assertThat(hotelService.getHistogram("amenities"))
                    .containsEntry("wifi", 10L);
            verify(hotelRepository).getHistogramByAmenities();
        }

        @Test
        void shouldIgnoreCase() {
            when(hotelRepository.getHistogramByBrand())
                    .thenReturn(List.<Object[]>of(new Object[]{"Hilton", 1L}));

            assertThat(hotelService.getHistogram("BRAND")).containsKey("Hilton");
            verify(hotelRepository).getHistogramByBrand();
        }

        @Test
        void shouldThrowOnUnknownParameter() {
            assertThatThrownBy(() -> hotelService.getHistogram("unknown"))
                    .isInstanceOf(BaseException.class)
                    .hasMessage("Unknown parameter");

            verifyNoInteractions(hotelRepository);
        }

        @Test
        void shouldReturnEmptyMapWhenNoData() {
            when(hotelRepository.getHistogramByBrand()).thenReturn(List.of());

            assertThat(hotelService.getHistogram("brand")).isEmpty();
        }

        @Test
        void shouldConvertKeyToString() {
            when(hotelRepository.getHistogramByBrand())
                    .thenReturn(List.<Object[]>of(new Object[]{123, 4L}));

            assertThat(hotelService.getHistogram("brand")).containsKey("123");
        }
    }

    @Nested
    @DisplayName("searchHotels")
    class SearchHotels {

        @Test
        void shouldReturnMappedList() {
            when(hotelRepository.findAll(any(Specification.class)))
                    .thenReturn(List.of(hotel));

            when(hotelMapper.toShortDto(hotel)).thenReturn(hotelShortDto);

            List<HotelShortDto> actual = hotelService.searchHotels(
                    "Grand", "Hilton", "Minsk", "Belarus", Set.of("wifi"));

            assertThat(actual).hasSize(1);
            assertThat(actual.get(0)).isEqualTo(hotelShortDto);
            verify(hotelRepository).findAll(any(Specification.class));
        }

        @Test
        void shouldReturnEmptyListWhenNoMatches() {
            when(hotelRepository.findAll(any(Specification.class)))
                    .thenReturn(List.of());

            assertThat(hotelService.searchHotels(null, null, null, null, null))
                    .isEmpty();
            verifyNoInteractions(hotelMapper);
        }

        @Test
        void shouldPassSpecificationToRepository() {
            when(hotelRepository.findAll(any(Specification.class)))
                    .thenReturn(List.of());

            hotelService.searchHotels("n", "b", "c", "co", Set.of("a"));

            verify(hotelRepository).findAll(any(Specification.class));
        }
    }
}