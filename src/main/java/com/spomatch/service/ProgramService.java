package com.spomatch.service;

import com.spomatch.dto.request.ProgramSearchRequestDTO;
import com.spomatch.dto.response.ProgramDetailResponseDTO;
import com.spomatch.dto.response.ProgramListResponseDTO;

public interface ProgramService {

    // 프로그램 목록 조회
    ProgramListResponseDTO getProgramList(ProgramSearchRequestDTO searchDTO);

    // 프로그램 상세 조회
    ProgramDetailResponseDTO getProgramDetail(Long programId);

    // 조회수 증가
    void increaseViewCount(Long programId);

    // 찜하기 토글
    boolean toggleLikeProgram(Long programId, String userId);

    // 프로그램 신청 가능 여부 확인
    boolean checkProgramAvailability(Long programId);

    // 프로그램 추천 목록 조회
    ProgramListResponseDTO getRecommendedPrograms(String userId, int limit);
}
