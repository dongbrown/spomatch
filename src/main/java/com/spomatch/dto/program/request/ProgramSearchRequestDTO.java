package com.spomatch.dto.program.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class ProgramSearchRequestDTO {
    private String region;           // 지역
    private String sportType;        // 종목
    private String ageType;          // 연령대
    private List<String> weekdays;   // 요일들
    private Integer minPrice;        // 최소 가격
    private Integer maxPrice;        // 최대 가격
    private LocalDate startDate;     // 시작일
    private LocalDate endDate;       // 종료일
    private String startTime;        // 시작 시간
    private String endTime;          // 종료 시간
    private String sortType;         // 정렬 기준
    private int page;                // 현재 페이지
    private int size;                // 페이지 크기
}

