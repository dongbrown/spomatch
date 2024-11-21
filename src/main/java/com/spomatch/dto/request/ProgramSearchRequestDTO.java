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
    // 검색 조건
    private String region;
    private String sportType;
    private String ageType;
    private List<String> weekdays;
    private Integer minPrice;
    private Integer maxPrice;
    private LocalDate startDate;
    private LocalDate endDate;
    private String sortType = "latest";  // 기본값 최신순

    // 페이징 정보
    private int page = 1;              // 현재 페이지 (기본값 1)
    private int size = 10;             // 페이지당 개수 (기본값 10)
    private PagingDTO paging;          // 페이징 정보를 담는 객체

    // 페이징 계산
    public void calculatePaging(int totalCount) {
        this.paging = new PagingDTO(page, size, totalCount);
    }

    // 페이징 정보 반환
    public PagingDTO getPaging() {
        return this.paging;
    }
}
