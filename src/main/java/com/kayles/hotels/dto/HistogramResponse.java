package com.kayles.hotels.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record HistogramResponse (
        Map<String, Long> data
) {
}
