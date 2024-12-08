package com.spomatch.dto;

import lombok.Getter;

@Getter
public class PagingDTO {
    private final int currentPage;
    private final int pageSize;
    private final int totalItems;
    private final int totalPages;
    private final int startIndex;

    public PagingDTO(int currentPage, int pageSize, int totalItems) {
        this.currentPage = currentPage > 0 ? currentPage : 1;
        this.pageSize = pageSize > 0 ? pageSize : 10;
        this.totalItems = totalItems;

        // 총 페이지 수 계산
        this.totalPages = (int) Math.ceil((double) totalItems / this.pageSize);

        // 시작 인덱스 계산
        this.startIndex = (this.currentPage - 1) * this.pageSize;
    }
}

