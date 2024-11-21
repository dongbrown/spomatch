package com.spomatch.service;

import com.spomatch.dao.ProgramDAO;
import com.spomatch.dto.ProgramDTO;
import com.spomatch.dto.request.ProgramSearchRequestDTO;
import com.spomatch.dto.response.ProgramDetailResponseDTO;
import com.spomatch.dto.response.ProgramListResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProgramServiceImpl implements ProgramService {

    private final ProgramDAO programDAO;

    @Override
    @Transactional
    public ProgramListResponseDTO getProgramList(ProgramSearchRequestDTO searchDTO) {
        // 전체 개수 조회
        int totalCount = programDAO.selectProgramCount(searchDTO);

        // 페이징 정보 설정
        searchDTO.calculatePaging(totalCount);

        // 프로그램 목록 조회
        List<ProgramDTO> programs = programDAO.selectProgramList(searchDTO);

        // 응답 DTO 생성
        return ProgramListResponseDTO.builder()
                .programs(programs)
                .paging(searchDTO.getPaging())
                .filters(getFilterOptions())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramDetailResponseDTO getProgramDetail(Long programId) {
        // 프로그램 기본 정보 조회
        ProgramDTO program = programDAO.selectProgramDetail(programId);
        if (program == null) {
            throw new RuntimeException("프로그램을 찾을 수 없습니다.");
        }

        // DetailResponse 생성
        return ProgramDetailResponseDTO.builder()
                .id(programId)
                .name(program.getProgramName())
                .facility(getFacilityInfo(program))
                .classInfo(getClassInfo(program))
                .build();
    }

    @Override
    @Transactional
    public void increaseViewCount(Long programId) {
        programDAO.updateViewCount(programId);
    }

    @Override
    @Transactional
    public boolean toggleLikeProgram(Long programId, String userId) {
        boolean isLiked = programDAO.checkLikeStatus(programId, userId);

        if (isLiked) {
            programDAO.deleteProgramLike(programId, userId);
        } else {
            programDAO.insertProgramLike(programId, userId);
        }

        return !isLiked;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkProgramAvailability(Long programId) {
        ProgramDTO program = programDAO.selectProgramDetail(programId);
        if (program == null) {
            return false;
        }
        return program.getProgramRecruitNumber() > 0; // 모집인원이 남아있는지 확인
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramListResponseDTO getRecommendedPrograms(String userId, int limit) {
        List<ProgramDTO> recommendedPrograms = programDAO.selectRecommendedPrograms(userId, limit);

        return ProgramListResponseDTO.builder()
                .programs(recommendedPrograms)
                .build();
    }

    // 필터 옵션 조회
    private Map<String, Object> getFilterOptions() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("cities", programDAO.selectCityList());         // 시도 목록
        filters.put("districts", programDAO.selectDistrictList());  // 시군구 목록
        filters.put("facilityTypes", programDAO.selectFacilityTypeList()); // 시설유형 목록
        filters.put("programTypes", programDAO.selectProgramTypeList());   // 프로그램유형 목록
        filters.put("targetAges", programDAO.selectTargetAgeList());      // 대상연령 목록
        return filters;
    }

    // 시설 정보 변환
    private ProgramDetailResponseDTO.FacilityInfo getFacilityInfo(ProgramDTO program) {
        return ProgramDetailResponseDTO.FacilityInfo.builder()
                .facilityName(program.getFacilityName())
                .facilityType(program.getFacilityTypeName())
                .address(program.getFacilityAddress())
                .contact(program.getFacilityPhoneNumber())
                .homepage(program.getHomepageUrl())
                .safetyManagement(program.getSafetyManagementContent())
                .build();
    }

    // 수업 정보 변환
    private ProgramDetailResponseDTO.ClassInfo getClassInfo(ProgramDTO program) {
        return ProgramDetailResponseDTO.ClassInfo.builder()
                .programType(program.getProgramTypeName())
                .programName(program.getProgramName())
                .targetAge(program.getProgramTargetName())
                .weekdays(List.of(program.getProgramOperationDays().split(",")))
                .time(program.getProgramOperationTime())
                .startDate(program.getProgramBeginDate())
                .endDate(program.getProgramEndDate())
                .price(program.getProgramPrice())
                .priceType(program.getProgramPriceTypeName())
                .recruitNumber(program.getProgramRecruitNumber())
                .educationGoal(program.getEducationGoalContent())
                .leaderQualification(program.getLeaderQualificationContent())
                .protectorParticipation(program.getProtectorParticipationYn())
                .build();
    }
}