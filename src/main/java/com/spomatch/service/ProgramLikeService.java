package com.spomatch.service;

import com.spomatch.dto.ProgramDTO;

import java.util.List;

public interface ProgramLikeService {
    // 찜하기 토글
    boolean toggleLikeProgram(Long programId, Long memberId);

    // 최근 찜하기 Redis에 추가
    void addRecentLike(Long programId);

    // 인기 프로그램 업데이트 (Redis & DB)
    void updateTopLikedPrograms();

    // 추천 프로그램 조회
    List<ProgramDTO> getRecommendedPrograms();

    // 찜하기 상태 확인
    boolean checkLikeStatus(Long programId, Long memberId);
}
