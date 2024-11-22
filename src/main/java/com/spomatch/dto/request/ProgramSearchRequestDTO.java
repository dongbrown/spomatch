package com.spomatch.dto.request;

import com.spomatch.common.paging.PagingDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class ProgramSearchRequestDTO {
    private String cityName;          // 시도명
    private String districtName;      // 시군구명
    private String facilityTypeName;  // 시설유형명
    private String programTypeName;   // 프로그램유형명
    private String programTargetName; // 프로그램대상명
    private Integer minPrice;
    private Integer maxPrice;
    private String startDate;
    private String endDate;
    private List<String> weekdays;
    private String sortBy = "latest";

    // 페이징 정보
    private int page = 1;
    private int size = 10;
    private PagingDTO paging;

    public void calculatePaging(int totalCount) {

    }
}