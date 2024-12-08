package com.spomatch.dto.request;

import com.spomatch.common.paging.PagingDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class ProgramSearchRequestDTO {
    // 검색 조건
    private String cityName;
    private String districtName;
    private String programTargetName;
    private List<String> weekdays;
    private Integer minPrice;
    private Integer maxPrice;
    private String sortBy = "latest";
    private List<String> events;

    // 페이징 관련
    private int page = 1;
    private int pageSize = 9;
    private PagingDTO paging;

    public void calculatePaging(int totalCount) {
        this.paging = new PagingDTO(page, pageSize, totalCount);
    }

    public int getStart() {
        return (page - 1) * pageSize;
    }

    // 검증 메소드 추가
    public void validate() {
        if (page < 1) {
            throw new IllegalArgumentException("페이지 번호는 1 이상이어야 합니다.");
        }
        if (pageSize < 1) {
            throw new IllegalArgumentException("페이지 크기는 1 이상이어야 합니다.");
        }
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("최소 가격이 최대 가격보다 클 수 없습니다.");
        }
    }
}