package com.spomatch.dao;

import com.spomatch.dto.ProgramDTO;
import com.spomatch.dto.request.ProgramSearchRequestDTO;
import com.spomatch.dto.response.ProgramListResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
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
    boolean checkLikeStatus(Long programId, Long userId);

    // 찜하기 추가
    void insertProgramLike(Long programId, Long userId);

    // 찜하기 삭제
    void deleteProgramLike(Long programId, Long userId);

    // 찜하기 개수 조회
    Long selectLikeCount(Long programId);

    // 신청 인원 조회
    Integer selectRegisterCount(Long programId);

    // 추천 프로그램 조회
    List<ProgramDTO> selectRecommendedPrograms(String userId, int limit);

    // 필터 옵션 조회
    List<String> selectCityList();         // 시도 목록
    List<String> selectDistrictList();     // 시군구 목록
    List<String> selectFacilityTypeList(); // 시설유형 목록
    List<String> selectProgramTypeList();  // 프로그램유형 목록
    List<String> selectTargetAgeList();    // 대상연령 목록

    List<ProgramDTO> selectLikedProgramList(Long memberId);
}