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
    // 프로그램 목록
    List<ProgramDTO> selectProgramList(ProgramSearchRequestDTO searchDTO);
    int selectFacilityCount(ProgramSearchRequestDTO searchDTO);
    ProgramDTO selectProgramDetail(Long programId);

    // 조회수
    void updateViewCount(Long programId);

    // 찜하기
    boolean checkLikeStatus(Long programId, Long memberId);
    void insertProgramLike(Long programId, Long memberId);
    void deleteProgramLike(Long programId, Long memberId);
    List<ProgramDTO> selectLikedProgramList(Long memberId);
    Long selectLikeCount(Long programId);

    // 신청
    Integer selectRegisterCount(Long programId);

    // 추천
    List<ProgramDTO> selectRecommendedPrograms(Long memberId, int limit);

    // 필터
    List<String> selectCityList();
    List<String> selectDistrictList();
    List<String> selectFacilityTypeList();
    List<String> selectProgramTypeList();
    List<String> selectTargetAgeList();
}