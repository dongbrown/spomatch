package com.spomatch.common.paging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class PagingDTO {
    private int currentPage;    // 현재 페이지
    private int pageSize;       // 페이지당 데이터 수
    private int totalCount;     // 전체 데이터 수
    private int totalPages;     // 전체 페이지 수
    private int startPage;      // 시작 페이지 번호
    private int endPage;        // 끝 페이지 번호
    private int start;          // 시작 행 번호
    private int end;            // 끝 행 번호
    private boolean hasPrevious;  // 이전 페이지 존재 여부
    private boolean hasNext;      // 다음 페이지 존재 여부
    private static final int PAGE_NAVIGATION_SIZE = 10;  // 페이지 네비게이션 크기

    public PagingDTO(int page, int size, int totalCount) {
        this.currentPage = page;
        this.pageSize = size;
        this.totalCount = totalCount;

        // 전체 페이지 수 계산
        this.totalPages = (int) Math.ceil((double) totalCount / size);
        if (this.totalPages == 0) {
            this.totalPages = 1;
        }

        // 현재 페이지가 범위를 벗어나지 않도록 조정
        this.currentPage = Math.max(1, Math.min(page, totalPages));

        // 시작 행과 끝 행 계산
        this.start = (this.currentPage - 1) * size;
        this.end = Math.min(this.currentPage * size, totalCount);

        // 시작 페이지와 끝 페이지 계산
        this.startPage = ((this.currentPage - 1) / PAGE_NAVIGATION_SIZE) * PAGE_NAVIGATION_SIZE + 1;
        this.endPage = Math.min(this.startPage + PAGE_NAVIGATION_SIZE - 1, totalPages);

        // 이전/다음 페이지 존재 여부
        this.hasPrevious = this.currentPage > 1;
        this.hasNext = this.currentPage < totalPages;
    }

    // 현재 페이지가 특정 페이지와 같은지 확인하는 메소드
    public boolean isCurrentPage(int pageNum) {
        return this.currentPage == pageNum;
    }

}
