package com.spomatch.common.paging;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PagingDTO {
    private int page;          // 현재 페이지
    private int size;          // 페이지당 개수
    private int totalCount;    // 전체 데이터 개수
    private int totalPages;    // 전체 페이지 수
    private int startRow;      // 시작 행
    private int endRow;        // 끝 행
    private int startPage;     // 시작 페이지 번호
    private int endPage;       // 끝 페이지 번호
    private boolean hasPrevious;  // 이전 페이지 존재 여부
    private boolean hasNext;      // 다음 페이지 존재 여부
    private static final int PAGE_NAVIGATION_SIZE = 10;  // 페이지 네비게이션 크기

    public PagingDTO(int page, int size, int totalCount) {
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;

        // 전체 페이지 수 계산
        this.totalPages = (totalCount + size - 1) / size;

        // 현재 페이지가 범위를 벗어나지 않도록 조정
        this.page = Math.max(1, Math.min(page, totalPages));

        // 시작 행과 끝 행 계산
        this.startRow = (this.page - 1) * size + 1;
        this.endRow = Math.min(this.page * size, totalCount);

        // 시작 페이지와 끝 페이지 계산
        this.startPage = ((this.page - 1) / PAGE_NAVIGATION_SIZE) * PAGE_NAVIGATION_SIZE + 1;
        this.endPage = Math.min(this.startPage + PAGE_NAVIGATION_SIZE - 1, totalPages);

        // 이전/다음 페이지 존재 여부 계산
        this.hasPrevious = this.page > 1;
        this.hasNext = this.page < totalPages;
    }

    // 페이지 네비게이션 시작번호 반환
    public int getStartPage() {
        return startPage;
    }

    // 페이지 네비게이션 끝번호 반환
    public int getEndPage() {
        return endPage;
    }

    // 이전 페이지 블록의 마지막 페이지 번호
    public int getPreviousPageBlock() {
        return startPage - 1;
    }

    // 다음 페이지 블록의 첫 페이지 번호
    public int getNextPageBlock() {
        return endPage + 1;
    }

    // 페이지 네비게이션 크기 반환
    public int getPageNavigationSize() {
        return PAGE_NAVIGATION_SIZE;
    }
}
