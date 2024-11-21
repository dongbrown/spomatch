package com.spomatch.dao.program;

import com.spomatch.dto.program.ProgramDTO;
import com.spomatch.dto.program.request.ProgramSearchRequestDTO;

import java.util.List;
import java.util.Map;

public interface ProgramDAO {
    // 프로그램 목록 조회
    List<ProgramDTO> selectProgramList(ProgramSearchRequestDTO searchDTO);

    // 프로그램 전체 개수 조회
    int selectProgramCount(ProgramSearchRequestDTO searchDTO);

    // 프로그램 상세 조회
    ProgramDTO selectProgramDetail(Long programId);

    // 조회수 증가
    void updateViewCount(Long programId);

    // 찜하기 상태 확인
    boolean checkLikeStatus(Long programId, String userId);

    // 찜하기 추가
    void insertProgramLike(Long programId, String userId);

    // 찜하기 삭제
    void deleteProgramLike(Long programId, String userId);

    // 찜하기 개수 조회
    Long selectLikeCount(Long programId);

    // 신청 인원 조회
    Integer selectRegisterCount(Long programId);

    // 추천 프로그램 조회
    List<ProgramDTO> selectRecommendedPrograms(String userId, int limit);

    // 필터 옵션 조회
    List<String> selectRegionList();
    List<String> selectSportTypeList();
    List<String> selectAgeTypeList();
}
