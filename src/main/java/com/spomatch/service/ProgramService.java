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

    /**
     * JSON 파일에서 프로그램 데이터를 읽어와 DB에 저장
     * @param filePath JSON 파일 경로
     * @throws RuntimeException JSON 파일 처리 중 오류 발생시
     */
    void importJsonData(String filePath);

    /**
     * 진행상황 조회
     * @return 현재 진행률 (0-100)
     */
    double getProgress();

    void updateCoordinates();
}
