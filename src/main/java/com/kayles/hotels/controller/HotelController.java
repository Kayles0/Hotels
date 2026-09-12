package com.kayles.hotels.controller;

import com.kayles.hotels.dto.HotelCreateDto;
import com.kayles.hotels.dto.HotelDto;
import com.kayles.hotels.dto.HotelShortDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Tag(name = "Hotel Management", description = "REST API for managing hotel properties")
@RequestMapping("/property-view")
public interface HotelController {

    @Operation(
            summary = "Get all hotels",
            description = "Returns a paginated list of all hotels with brief information including id, name, description, address, and phone."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved hotel list"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/hotels")
    ResponseEntity<Page<HotelShortDto>> getAllHotels(
            @Parameter(description = "Pagination parameters (page, size, sort)")
            @PageableDefault(size = 10) Pageable pageable
    );

    @Operation(
            summary = "Get hotel by ID",
            description = "Returns detailed information about a specific hotel including address, contacts, arrival time, and amenities."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved hotel details"),
            @ApiResponse(responseCode = "404", description = "Hotel not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/hotels/{id}")
    ResponseEntity<HotelDto> getHotelById(
            @Parameter(description = "Hotel ID", required = true, example = "1")
            @PathVariable Long id
    );

    @Operation(
            summary = "Search hotels",
            description = "Searches hotels by optional filters: name, brand, city, country, and amenities. "
                    + "All parameters are optional and can be combined. Search is case-insensitive and uses partial matching."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved matching hotels"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    ResponseEntity<Page<HotelShortDto>> searchHotels(
            @Parameter(description = "Hotel name (partial match, case-insensitive)", example = "Hilton")
            @RequestParam(required = false) String name,

            @Parameter(description = "Hotel brand (partial match, case-insensitive)", example = "Hilton")
            @RequestParam(required = false) String brand,

            @Parameter(description = "City name (partial match, case-insensitive)", example = "Minsk")
            @RequestParam(required = false) String city,

            @Parameter(description = "Country name (partial match, case-insensitive)", example = "Belarus")
            @RequestParam(required = false) String country,

            @Parameter(description = "Set of required amenities", example = "[\"Free WiFi\", \"Pool\"]")
            @RequestParam(required = false) Set<String> amenities,

            @Parameter(description = "Pagination parameters (page, size, sort)")
            @PageableDefault(size = 10) Pageable pageable
    );

    @Operation(
            summary = "Create a new hotel",
            description = "Creates a new hotel with the provided details. The hotel name must be unique. "
                    + "Returns the created hotel with brief information."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Hotel successfully created",
                    content = @Content(schema = @Schema(implementation = HotelShortDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data (validation failed)"),
            @ApiResponse(responseCode = "409", description = "Hotel with the given name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/hotels")
    ResponseEntity<HotelShortDto> createHotel(
            @Parameter(description = "Hotel creation data", required = true)
            @Valid @RequestBody HotelCreateDto dto
    );

    @Operation(
            summary = "Add amenities to a hotel",
            description = "Adds a list of amenities to an existing hotel. Duplicate amenities are ignored. "
                    + "Returns the updated hotel with full details."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Amenities successfully added",
                    content = @Content(schema = @Schema(implementation = HotelDto.class))),
            @ApiResponse(responseCode = "404", description = "Hotel not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/hotels/{id}/amenities")
    ResponseEntity<HotelDto> addAmenities(
            @Parameter(description = "Hotel ID", required = true, example = "1")
            @PathVariable Long id,

            @Parameter(description = "List of amenities to add", required = true,
                    array = @ArraySchema(schema = @Schema(type = "string")),
                    examples = @ExampleObject(value = "[\"Free WiFi\", \"Pool\", \"Spa\"]"))
            @RequestBody Set<String> amenities
    );

    @Operation(
            summary = "Get hotel histogram",
            description = "Returns the count of hotels grouped by the specified parameter. "
                    + "Supported parameters: brand, city, country, amenities."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved histogram",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "City histogram example",
                                    value = "{\"Minsk\": 2, \"Brest\": 1}"
                            )
                    )),
            @ApiResponse(responseCode = "400", description = "Unknown parameter. Supported values: brand, city, country, amenities"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/histogram/{param}")
    ResponseEntity<Map<String, Long>> getHistogram(
            @Parameter(description = "Grouping parameter", required = true,
                    schema = @Schema(allowableValues = {"brand", "city", "country", "amenities"}),
                    example = "city")
            @PathVariable String param
    );
}
