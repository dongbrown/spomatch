package com.spomatch.service;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spomatch.dao.ProgramDAO;
import com.spomatch.dto.ProgramDTO;
import com.spomatch.dto.request.ProgramSearchRequestDTO;
import com.spomatch.dto.response.ProgramDetailResponseDTO;
import com.spomatch.dto.response.ProgramListResponseDTO;
import com.spomatch.entity.SportsFacilityProgram;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProgramServiceImpl implements ProgramService {

    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;

    private double currentProgress = 0.0;

    private final ProgramDAO programDAO;
//    private final LocationService locationService;
//    private final ReviewService reviewService;

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

//        // 위치 정보 조회
//        ProgramDetailResponseDTO.LocationInfo locationInfo =
//                locationService.getLocationInfo(program.getFacilityId());
//
//        // 리뷰 정보 조회
//        List<ProgramDetailResponseDTO.ReviewInfo> reviews =
//                reviewService.getProgramReviews(programId);

        // 통계 정보 조회
//        ProgramDetailResponseDTO.StatisticsInfo statistics =
//                getStatisticsInfo(programId);

        // DetailResponse 생성
        return ProgramDetailResponseDTO.builder()
                .id(program.getId())
                .name(program.getName())
                .facility(getFacilityInfo(program))
                .classInfo(getClassInfo(program))
//                .statistics(statistics)
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
        // 이미 찜한 상태인지 확인
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
        return program.getCurrentParticipants() < program.getMaxParticipants();
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramListResponseDTO getRecommendedPrograms(String userId, int limit) {
        List<ProgramDTO> recommendedPrograms =
                programDAO.selectRecommendedPrograms(userId, limit);

        return ProgramListResponseDTO.builder()
                .programs(recommendedPrograms)
                .build();
    }

    // 필터 옵션 조회
    private Map<String, Object> getFilterOptions() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("regions", programDAO.selectRegionList());
        filters.put("sportTypes", programDAO.selectSportTypeList());
        filters.put("ageTypes", programDAO.selectAgeTypeList());
        return filters;
    }

    // 상세 정보 변환 메서드들
    private ProgramDetailResponseDTO.FacilityInfo getFacilityInfo(ProgramDTO program) {
        return ProgramDetailResponseDTO.FacilityInfo.builder()
                .facilityName(program.getFacility())
//                .facilityType(program.getFacilityType())
//                .contact(program.getContact())
//                .homepage(program.getHomepage())
                .build();
    }

    private ProgramDetailResponseDTO.ClassInfo getClassInfo(ProgramDTO program) {
        return ProgramDetailResponseDTO.ClassInfo.builder()
                .sportType(program.getSportType())
                .ageType(program.getAgeType())
                .weekdays(program.getWeekdays())
                .time(program.getTime())
                .startDate(program.getStartDate())
                .endDate(program.getEndDate())
                .price(program.getPrice())
                .currentParticipants(program.getCurrentParticipants())
                .maxParticipants(program.getMaxParticipants())
                .build();
    }

//    private ProgramDetailResponseDTO.StatisticsInfo getStatisticsInfo(Long programId) {
//        return ProgramDetailResponseDTO.StatisticsInfo.builder()
//                .viewCount(programDAO.selectViewCount(programId))
//                .likeCount(programDAO.selectLikeCount(programId))
//                .rating(reviewService.getAverageRating(programId))
//                .reviewCount(reviewService.getReviewCount(programId))
//                .registerCount(programDAO.selectRegisterCount(programId))
//                .build();
//    }


    @Override
    @Transactional
    public void importJsonData(String filePath) {
        try {
            File file = new File(filePath);

            List<SportsFacilityProgram> programs = Arrays.asList(
                    objectMapper.readValue(file, SportsFacilityProgram[].class));

            int batchSize = 1000;
            int totalSize = programs.size();
            long startTime = System.currentTimeMillis();

            for (int i = 0; i < totalSize; i += batchSize) {
                List<SportsFacilityProgram> batch = programs.subList(
                        i, Math.min(totalSize, i + batchSize)
                );

                for (SportsFacilityProgram program : batch) {
                    entityManager.persist(program);
                }

                entityManager.flush();
                entityManager.clear();

                int processedCount = Math.min(i + batchSize, totalSize);
                updateAndLogProgress(startTime, processedCount, totalSize);
            }

            long totalTime = (System.currentTimeMillis() - startTime) / 1000;
            log.info("Import completed. Total time: {}s", totalTime);

        } catch (Exception e) {
            log.error("Failed to import JSON", e);
            throw new RuntimeException("JSON 파일 처리 중 오류 발생", e);
        }
    }

    @Override
    public double getProgress() {
        return currentProgress;
    }

    private void updateAndLogProgress(long startTime, int processedCount, int totalSize) {
        long currentTime = System.currentTimeMillis();
        long elapsedSeconds = (currentTime - startTime) / 1000;
        double recordsPerSecond = elapsedSeconds > 0 ? processedCount / (double) elapsedSeconds : 0;

        currentProgress = (processedCount * 100.0) / totalSize;

        log.info("Processed: {}/{} ({}%) - {} records/sec",
                processedCount,
                totalSize,
                String.format("%.2f", currentProgress),
                String.format("%.2f", recordsPerSecond)
        );
    }
}