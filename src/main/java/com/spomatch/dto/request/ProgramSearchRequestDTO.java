package com.spomatch.dto.request;

import com.spomatch.common.paging.PagingDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class ProgramSearchRequestDTO {
    private String cityName;
    private String districtName;
    private String programTargetName;
    private List<String> weekdays;
    private Integer minPrice;
    private Integer maxPrice;
    private String sortBy = "latest";  // 기본값
    private List<String> events; //종목

    // 페이징 관련 필드
    private int page = 1;            // 기본값 1페이지
    private int size = 10;           // 기본값 10개
    private PagingDTO paging;        // 페이징 정보

    // 페이징 계산
    public void calculatePaging(int totalCount) {
        this.paging = new PagingDTO(page, size, totalCount);
    }

    // 시작행, 종료행 계산 메서드 추가
    public int getStart() {
        return (page - 1) * size;
    }

    public int getEnd() {
        return page * size;
    }
}
