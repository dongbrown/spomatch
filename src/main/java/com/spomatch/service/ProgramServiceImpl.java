package com.spomatch.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spomatch.common.paging.PagingDTO;
import com.spomatch.dao.ProgramDAO;
import com.spomatch.dto.ProgramDTO;
import com.spomatch.dto.request.ProgramSearchRequestDTO;
import com.spomatch.dto.response.ProgramDetailResponseDTO;
import com.spomatch.dto.response.ProgramListResponseDTO;
import com.spomatch.entity.SportsFacilityProgram;
import com.spomatch.repository.ProgramRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProgramServiceImpl implements ProgramService {

    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;
    private final ProgramDAO programDAO;
    private final ProgramRepository programRepository;
    private double currentProgress = 0.0;

    // WebClient(비동기 처리) + CompletableFuture를 위한 코드 (속도 개선)
    private final WebClient webClient;
    private static final int BATCH_SIZE = 10;


    @Value("${naver.maps.client-id}")
    private String clientId;

    @Value("${naver.maps.client-secret}")
    private String clientSecret;

    @Override
    @Transactional(readOnly = true)
    public ProgramListResponseDTO getProgramList(ProgramSearchRequestDTO searchDTO) {
        // 시설 기준으로 전체 개수 조회 (페이징용)
        int totalFacilities = programDAO.selectFacilityCount(searchDTO);

        // 페이징 정보 계산
        searchDTO.calculatePaging(totalFacilities);

        // 필터링된 프로그램 목록 조회
        List<ProgramDTO> programs = programDAO.selectProgramList(searchDTO);

        // 필터 옵션 조회
        Map<String, Object> filterOptions = getFilterOptions();

        // 결과가 없는 경우 빈 목록 반환
        if (programs == null) {
            programs = Collections.emptyList();
        }

        // 응답 DTO 생성
        return ProgramListResponseDTO.builder()
                .programs(programs)
                .paging(searchDTO.getPaging())
                .filters(filterOptions)
                .build();
    }



    // 페이징 결과 검증
    private void validatePagingResult(PagingDTO paging, List<ProgramDTO> programs) {
        if (paging == null) {
            throw new IllegalStateException("페이징 정보가 없습니다.");
        }

        if (programs == null) {
            throw new IllegalStateException("프로그램 목록이 null입니다.");
        }

        if (paging.getCurrentPage() > paging.getTotalPages() && paging.getTotalPages() > 0) {
            throw new IllegalArgumentException("요청한 페이지 번호가 총 페이지 수를 초과했습니다.");
        }
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
                .id(program.getId())
                .programName(program.getProgramName())
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
    public boolean toggleLikeProgram(Long programId, Long userId) {
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
        return program.getProgramRecruitNumber() > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramListResponseDTO getRecommendedPrograms(Long userId, int limit) {
        List<ProgramDTO> recommendedPrograms = programDAO.selectRecommendedPrograms(userId, limit);
        return ProgramListResponseDTO.builder()
                .programs(recommendedPrograms)
                .build();
    }

    private Map<String, Object> getFilterOptions() {
        Map<String, Object> filters = new HashMap<>();

        try {
            filters.put("cities", programDAO.selectCityList());
            filters.put("districts", programDAO.selectDistrictList());
            filters.put("facilityTypes", programDAO.selectFacilityTypeList());
            filters.put("programTypes", programDAO.selectProgramTypeList());
            filters.put("targetAges", programDAO.selectTargetAgeList());

            // 종목 리스트 추가
            List<String> events = Arrays.asList(
                    "수영", "골프", "탁구", "배드민턴", "댄스",
                    "필라테스", "헬스", "테니스", "에어로빅", "기타"
            );
            filters.put("events", events);
        } catch (Exception e) {
            log.error("필터 옵션 조회 중 오류 발생", e);
            // 에러 발생 시 빈 맵 반환
            return Collections.emptyMap();
        }

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
                .targetName(program.getProgramTargetName())
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

    @Override
    @Transactional
    public void importJsonData(String filePath) {
        StringBuilder summary = new StringBuilder();
        int coordinatesUpdated = 0;

        try {
            Resource resource = new ClassPathResource("data/publicProgram.json");

            // 1. JSON 파일 읽기
            List<SportsFacilityProgram> initialPrograms = Arrays.asList(
                    objectMapper.readValue(resource.getInputStream(), SportsFacilityProgram[].class));

            // 2. 중복 확인을 위한 맵 생성
            Map<String, List<SportsFacilityProgram>> duplicatesMap = initialPrograms.stream()
                    .collect(Collectors.groupingBy(this::generateCompositeId));

            // 중복 프로그램 정보 수집
            List<String> duplicateInfos = new ArrayList<>();
            duplicatesMap.forEach((compositeId, programs) -> {
                if (programs.size() > 1) {
                    SportsFacilityProgram program = programs.get(0);
                    duplicateInfos.add(String.format("시설: %s, 프로그램: %s, 시작일: %s, 시간: %s (%d개)",
                            program.getFacilityName(),
                            program.getProgramName(),
                            program.getProgramBeginDate(),
                            program.getProgramOperationTime(),
                            programs.size()));
                }
            });

            // 중복 제거된 프로그램 리스트
            List<SportsFacilityProgram> jsonPrograms = duplicatesMap.values().stream()
                    .map(list -> list.get(0))
                    .collect(Collectors.toList());

            // 기존 데이터 확인 및 새로운 데이터 필터링
            Set<String> existingCompositeIds = programRepository.findAll().stream()
                    .map(this::generateCompositeId)
                    .collect(Collectors.toSet());

            List<SportsFacilityProgram> newPrograms = jsonPrograms.stream()
                    .filter(program -> !existingCompositeIds.contains(generateCompositeId(program)))
                    .collect(Collectors.toList());

            // 데이터 저장
            int savedCount = 0;
            if (!newPrograms.isEmpty()) {
                int batchSize = 1000;
                int totalSize = newPrograms.size();
                long startTime = System.currentTimeMillis();

                for (int i = 0; i < totalSize; i += batchSize) {
                    List<SportsFacilityProgram> batch = newPrograms.subList(
                            i, Math.min(totalSize, i + batchSize)
                    );

                    for (SportsFacilityProgram program : batch) {
                        entityManager.persist(program);
                        savedCount++;
                    }

                    entityManager.flush();
                    entityManager.clear();

                    updateAndLogProgress(startTime, i + batch.size(), totalSize);
                }
            }

            // 종합 결과 로그 출력
            summary.append("\n=== 데이터 임포트 결과 요약 ===\n");
            summary.append(String.format("- 전체 읽은 데이터: %d개\n", initialPrograms.size()));
            summary.append(String.format("- 중복 제거 후 데이터: %d개\n", jsonPrograms.size()));
            summary.append(String.format("- 기존 DB 데이터: %d개\n", existingCompositeIds.size()));
            summary.append(String.format("- 새로 저장된 데이터: %d개\n", savedCount));

            if (!duplicateInfos.isEmpty()) {
                summary.append("\n=== 중복 데이터 목록 ===\n");
                duplicateInfos.forEach(info -> summary.append("- ").append(info).append("\n"));
            }

            // 좌표 업데이트 실행 및 결과 추가
            if (savedCount > 0) {
                List<SportsFacilityProgram> facilitiesWithoutCoords = programRepository.findByLatitudeIsNull();
                coordinatesUpdated = facilitiesWithoutCoords.size();

                summary.append("\n=== 좌표 업데이트 결과 ===\n");
                summary.append(String.format("- 좌표 업데이트 대상: %d개\n", coordinatesUpdated));

                updateCoordinates();  // 좌표 업데이트 실행
            }

            summary.append("\n=== 전체 작업 완료 ===");
            log.info(summary.toString());

        } catch (Exception e) {
            log.error("Failed to import JSON", e);
            throw new RuntimeException("JSON 파일 처리 중 오류 발생", e);
        }
    }


    // 중복 제거를 위한 helper 메서드
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }


    private String generateCompositeId(SportsFacilityProgram program) {
        return String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s",
                program.getFacilityName(),          // 시설명 (FCLTY_NM)
                program.getDistrictName(),          // 구군명 (SIGNGU_NM)
                program.getFacilityTypeName(),      // 시설유형명 (FCLTY_TY_NM)
                program.getCityName(),              // 시도명 (CTPRVN_NM)
                program.getProgramName(),           // 프로그램명 (PROGRM_NM)
                program.getProgramTypeName(),       // 프로그램 유형명 (PROGRM_TY_NM)
                program.getProgramTargetName(),     // 프로그램 대상명 (PROGRM_TRGET_NM)
                program.getProgramBeginDate(),      // 프로그램 시작일자 (PROGRM_BEGIN_DE)
                program.getProgramEndDate(),        // 프로그램 종료일자 (PROGRM_END_DE)
                program.getProgramOperationDays(),  // 프로그램 개설요일명 (PROGRM_ESTBL_WKDAY_NM)
                program.getProgramOperationTime(),  // 프로그램 개설시간대값 (PROGRM_ESTBL_TIZN_VALUE)
                program.getProgramPrice(),          // 프로그램 가격 (PROGRM_PRC)
                program.getProgramPriceTypeName(),  // 프로그램가격유형명 (PROGRM_PRC_TY_NM)
                program.getProgramRecruitNumber()   // 프로그램 모집인원수 (PROGRM_RCRIT_NMPR_CO)
        ).toLowerCase().replaceAll("\\s+", "");
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

    //시설 이름으로 위도/경도 추출 후 db에 저장하는 기능
    @Override
    @Transactional
    public void updateCoordinates() {
        List<SportsFacilityProgram> facilities = programRepository.findByLatitudeIsNull();
        log.info("Found {} facilities without coordinates", facilities.size());


        // 좌표가 없는 데이터가 없으면 메서드 종료
        if (facilities.isEmpty()) {
            log.info("No facilities need coordinate updates");
            return;
        }

        facilities.parallelStream().forEach(facility -> {
            try {
                // 주소가 null인 경우 스킵
                if (facility.getFacilityAddress() == null) {
                    log.warn("Skipping facility '{}': No address available", facility.getFacilityName());
                    return;
                }

                // 이미 좌표가 있는 경우 스킵 (안전장치)
                if (facility.getLatitude() != null && facility.getLongitude() != null) {
                    log.info("Skipping facility '{}': Coordinates already exist", facility.getFacilityName());
                    return;
                }

                Thread.sleep(50);
                String fullAddress = cleanAddress(facility);
                log.info("Processing facility '{}' with address: {}", facility.getFacilityName(), fullAddress);

                String address = URLEncoder.encode(fullAddress, StandardCharsets.UTF_8);
                String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + address;
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
                conn.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

                ObjectMapper mapper = new ObjectMapper();
                JsonNode response = mapper.readTree(conn.getInputStream());

                JsonNode addresses = response.get("addresses");
                if (addresses != null && addresses.size() > 0) {
                    JsonNode addressNode = addresses.get(0);
                    double latitude = Double.parseDouble(addressNode.get("y").asText());
                    double longitude = Double.parseDouble(addressNode.get("x").asText());

                    log.info("Got coordinates for facility '{}': lat={}, lng={}",
                            facility.getFacilityName(), latitude, longitude);

                    facility.setLatitude(latitude);
                    facility.setLongitude(longitude);
                    programRepository.save(facility);
                    log.info("Successfully updated coordinates for facility: {}", facility.getFacilityName());
                } else {
                    log.warn("No coordinates found for facility '{}' with address: {}",
                            facility.getFacilityName(), fullAddress);
                }
            } catch (Exception e) {
                log.error("Error updating coordinates for facility '{}': {}",
                        facility.getFacilityName(), e.getMessage());
            }
        });
    }


    @Override
    public List<ProgramDTO> selectLikedProgramList(Long memberId) {
        List<ProgramDTO> likedPrograms = programDAO.selectLikedProgramList(memberId);
        return likedPrograms;
    }

    //정규식으로 데이터 정제 ..
    private String cleanAddress(SportsFacilityProgram facility) {
        String cityName = facility.getCityName().trim();
        String address = facility.getFacilityAddress();
        if (address == null) {
            // NULL인 경우 시설 이름 + 구 정보로 대체
            address = facility.getCityName() + " " +
                    facility.getDistrictName() + " " +
                    facility.getFacilityName();
        }
        // 중복된 시/도명 제거
        address = address.replace(cityName, "").trim();

        // 중복된 구/군명 제거
        String districtName = facility.getDistrictName().trim();
        address = address.replace(districtName, "").trim();

        // 괄호 내용 제거
        address = address.replaceAll("\\(.*\\)", "").trim();

        // 연속된 공백 제거
        address = address.replaceAll("\\s+", " ").trim();

        // 최종 주소 조합
        return cityName + " " + districtName + " " + address;
    }
}