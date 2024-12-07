package com.spomatch.service;

import com.spomatch.dto.FacilityMapDTO;
import com.spomatch.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MapServiceImpl implements MapService {

    private final ProgramRepository programRepository;
    private final CacheManager cacheManager;

    @Value("${app.map.page-size:1000}")
    private int pageSize;

    @Cacheable(value = "facilityCache",
            key = "#minLat + '_' + #maxLat + '_' + #minLng + '_' + #maxLng + '_' + #zoom + '_' + #page",
            unless = "#result.isEmpty()")
    @Transactional(readOnly = true)
    public List<FacilityMapDTO> getFacilitiesInBounds(
            double minLat, double maxLat, double minLng, double maxLng, int zoom, int page) {

        if (zoom <= 6) {
            return Collections.emptyList();
        }

        int offset = page * pageSize;

        List<Object[]> results = programRepository.findFacilitiesInBoundingBox(
                minLat, maxLat, minLng, maxLng, offset, pageSize
        );

        return results.stream()
                .filter(Objects::nonNull)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private FacilityMapDTO convertToDTO(Object[] row) {
        return FacilityMapDTO.builder()
                .id(((Number) row[0]).longValue())
                .facilityName((String) row[1])
                .programName((String) row[2])
                .latitude((Double) row[3])
                .longitude((Double) row[4])
                .cityName((String) row[5])
                .districtName((String) row[6])
                .address((String) row[7])
                .phoneNumber((String) row[8])
                .beginDate((String) row[9])
                .endDate((String) row[10])
                .price((BigDecimal) row[11])
                .recruitNumber((BigDecimal) row[12])
                .homepageUrl((String) row[13])
                .targetName((String) row[14])
                .operationDays((String) row[15])
                .operationTime((String) row[16])
                .build();
    }
}