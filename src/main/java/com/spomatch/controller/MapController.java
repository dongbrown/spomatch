package com.spomatch.controller;

import com.spomatch.entity.SportsFacilityProgram;
import com.spomatch.repository.ProgramRepository;
import com.spomatch.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
public class MapController {
    private final MapService mapService;
    private final ProgramRepository programRepository;

    @GetMapping("/navermap")
    public String showNaverMap(Model model) {
        return "/navermap";
    }

    @GetMapping("/api/facilities")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getFacilities(
            @RequestParam(required = false, defaultValue = "0") double minLat,
            @RequestParam(required = false, defaultValue = "0") double maxLat,
            @RequestParam(required = false, defaultValue = "0") double minLng,
            @RequestParam(required = false, defaultValue = "0") double maxLng) {

        List<SportsFacilityProgram> facilities;
        if (minLat == 0 && maxLat == 0 && minLng == 0 && maxLng == 0) {
            facilities = programRepository.findAll();
        } else {
            facilities = programRepository.findByLatitudeBetweenAndLongitudeBetween(
                    minLat, maxLat, minLng, maxLng
            );
        }

        List<Map<String, Object>> response = facilities.stream()
                .map(facility -> {
                    Map<String, Object> map = new HashMap<>();
                    // 클라이언트가 기대하는 필드명으로 매핑
                    map.put("PROGRM_ID", facility.getId());
                    map.put("FCLTY_NM", facility.getFacilityName());
                    map.put("PROGRM_NM", facility.getProgramName());
                    map.put("latitude", facility.getLatitude());
                    map.put("longitude", facility.getLongitude());
                    map.put("CTPRVN_NM", facility.getCityName());
                    map.put("SIGNGU_NM", facility.getDistrictName());
                    map.put("FCLTY_ADDR", facility.getFacilityAddress());
                    map.put("FCLTY_TEL_NO", facility.getFacilityPhoneNumber());
                    map.put("PROGRM_BEGIN_DE", facility.getProgramBeginDate());
                    map.put("PROGRM_END_DE", facility.getProgramEndDate());
                    map.put("PROGRM_PRC", facility.getProgramPrice());
                    map.put("PROGRM_RCRIT_NMPR_CO", facility.getProgramRecruitNumber());
                    map.put("HMPG_URL", facility.getHomepageUrl());
                    map.put("PROGRM_TRGET_NM", facility.getProgramTargetName());
                    map.put("PROGRM_ESTBL_WKDAY_NM", facility.getProgramOperationDays());
                    map.put("PROGRM_ESTBL_TIZN_VALUE", facility.getProgramOperationTime());
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}