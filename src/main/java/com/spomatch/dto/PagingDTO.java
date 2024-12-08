package com.spomatch.dto;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class PagingDTO {
    private final int currentPage;
    private final int pageSize;
    private final int totalItems;
    private final int totalPages;
    private final int startPage;
    private final int endPage;
    private final boolean hasPrevious;
    private final boolean hasNext;
    private final List<Integer> pageNumbers;

    public PagingDTO(int currentPage, int pageSize, int totalItems) {
        this.currentPage = Math.max(currentPage, 1);
        this.pageSize = Math.max(pageSize, 1);
        this.totalItems = Math.max(totalItems, 0);

        // 총 페이지 수 계산
        this.totalPages = (int) Math.ceil((double) totalItems / this.pageSize);

        // 화면에 보여질 페이지 번호 수
        int displayPageNum = 10;

        // 현재 페이지 그룹의 시작과 끝 페이지 계산
        this.startPage = ((currentPage - 1) / displayPageNum) * displayPageNum + 1;
        this.endPage = Math.min(startPage + displayPageNum - 1, totalPages);

        // 이전/다음 페이지 존재 여부
        this.hasPrevious = currentPage > 1;
        this.hasNext = currentPage < totalPages;

        // 페이지 번호 리스트 생성
        this.pageNumbers = IntStream.rangeClosed(startPage, endPage)
                .boxed()
                .collect(Collectors.toList());
    }

    // 페이지 관련 유틸리티 메소드
    public boolean hasContent() {
        return totalItems > 0;
    }

    public boolean isFirst() {
        return currentPage == 1;
    }

    public boolean isLast() {
        return currentPage == totalPages;
    }

    public int getOffset() {
        return (currentPage - 1) * pageSize;
    }
}

