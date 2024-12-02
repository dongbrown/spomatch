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
    private String sortBy = "latest";  // 기본값
    private List<String> events; // 종목

    // 페이징 관련 필드
    private int page = 1;            // 기본값 1페이지
    private int pageSize = 9;        // 기본값 9개
    private PagingDTO paging;        // 페이징 정보
    private int start;               // 시작 인덱스

    // 페이징 계산
    public void calculatePaging(int totalCount) {
        this.paging = new PagingDTO(page, pageSize, totalCount);
        this.start = (page - 1) * pageSize;
    }

    // SQL limit 구문을 위한 getter
    public int getStart() {
        return (page - 1) * pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    // 기존 Size setter를 pageSize setter로 변경
    public void setSize(int size) {
        this.pageSize = size;
    }

    // 기존 size getter도 pageSize를 반환하도록
    public int getSize() {
        return pageSize;
    }
}