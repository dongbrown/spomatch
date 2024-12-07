package com.spomatch.service;

import com.spomatch.dto.FacilityMapDTO;

import java.util.List;

public interface MapService {
    List<FacilityMapDTO> getFacilitiesInBounds(
            double minLat,
            double maxLat,
            double minLng,
            double maxLng,
            int zoom,
            int page
    );
}