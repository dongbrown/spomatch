//package com.spomatch.service;
//
//import com.spomatch.dto.ProgramDTO;
//import com.spomatch.entity.ProgramLike;
//import com.spomatch.repository.ProgramRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.ListOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class ProgramLikeServiceImpl implements ProgramLikeService {
//    private final RedisTemplate<String, Long> redisTemplate;
//    private final ProgramRepository programRepository;
//    private final ProgramLikeRepository programLikeRepository;
//    private static final String RECENT_LIKES_KEY = "recent:likes";
//    private static final String TOP_PROGRAMS_KEY = "top:liked:programs";
//    private static final int MAX_LIST_SIZE = 1000;
//
//    @Override
//    @Transactional
//    public boolean toggleLikeProgram(Long programId, Long memberId) {
//        boolean isLiked = checkLikeStatus(programId, memberId);
//
//        if (isLiked) {
//            programLikeRepository.deleteByProgramIdAndMemberId(programId, memberId);
//        } else {
//            ProgramLike like = ProgramLike.builder()
//                    .program(programRepository.findById(programId).orElseThrow())
//                    .memberId(memberId)
//                    .createdAt(LocalDateTime.now())
//                    .build();
//            programLikeRepository.save(like);
//
//            // Redis에 최근 찜하기 추가
//            addRecentLike(programId);
//        }
//
//        return !isLiked;
//    }
//
//    @Override
//    @Scheduled(fixedRate = 6000)
//    public void addRecentLike(Long programId) {
//        ListOperations<String, Long> listOps = redisTemplate.opsForList();
//
//        listOps.rightPush(RECENT_LIKES_KEY, programId);
//
//        Long size = listOps.size(RECENT_LIKES_KEY);
//        if (size != null && size > MAX_LIST_SIZE) {
//            listOps.leftPop(RECENT_LIKES_KEY);
//        }
//
//        log.info("Added recent like for program: {}", programId);
//    }
//
//    @Override
//    @Scheduled(cron = "0 0 * * * *")
//    public void updateTopLikedPrograms() {
//        try {
//            ListOperations<String, Long> listOps = redisTemplate.opsForList();
//            List<Long> recentLikes = listOps.range(RECENT_LIKES_KEY, 0, -1);
//
//            if (recentLikes == null || recentLikes.isEmpty()) {
//                log.info("No recent likes found");
//                return;
//            }
//
//            Map<Long, Long> frequencyMap = recentLikes.stream()
//                    .collect(Collectors.groupingBy(
//                            Function.identity(),
//                            Collectors.counting()
//                    ));
//
//            List<Long> topPrograms = frequencyMap.entrySet().stream()
//                    .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
//                    .limit(100)
//                    .map(Map.Entry::getKey)
//                    .collect(Collectors.toList());
//
//            String programIdsStr = String.join(",", topPrograms.stream()
//                    .map(String::valueOf)
//                    .collect(Collectors.toList()));
//
//            // Redis 업데이트
//            redisTemplate.opsForValue().set(TOP_PROGRAMS_KEY, programIdsStr);
//
//            // DB 백업
//            saveTopProgramsToDb(programIdsStr);
//
//            log.info("Updated top liked programs. Count: {}", topPrograms.size());
//        } catch (Exception e) {
//            log.error("Error updating top liked programs", e);
//        }
//    }
//
//    @Override
//    public List<ProgramDTO> getRecommendedPrograms() {
//        try {
//            String topProgramsStr = redisTemplate.opsForValue().get(TOP_PROGRAMS_KEY);
//
//            if (topProgramsStr == null) {
//                log.info("No cached recommendations found, fetching from DB");
//                return getRecommendedProgramsFromDb();
//            }
//
//            List<Long> programIds = Arrays.stream(topProgramsStr.split(","))
//                    .map(Long::parseLong)
//                    .collect(Collectors.toList());
//
//            return programRepository.findAllById(programIds).stream()
//                    .map(this::convertToDTO)
//                    .collect(Collectors.toList());
//        } catch (Exception e) {
//            log.error("Error getting recommended programs", e);
//            return getRecommendedProgramsFromDb();
//        }
//    }
//
//    @Override
//    public boolean checkLikeStatus(Long programId, Long memberId) {
//        return programLikeRepository.existsByProgramIdAndMemberId(programId, memberId);
//    }
//
//    private void saveTopProgramsToDb(String programIds) {
//        PopularPrograms popularPrograms = PopularPrograms.builder()
//                .programIds(programIds)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        popularProgramsRepository.save(popularPrograms);
//    }
//
//    private List<ProgramDTO> getRecommendedProgramsFromDb() {
//        // DB에서 최신 인기 프로그램 목록 조회 로직
//        return popularProgramsRepository.findTopByOrderByCreatedAtDesc()
//                .map(pp -> Arrays.stream(pp.getProgramIds().split(","))
//                        .map(Long::parseLong)
//                        .map(id -> programRepository.findById(id).orElse(null))
//                        .filter(Objects::nonNull)
//                        .map(this::convertToDTO)
//                        .collect(Collectors.toList()))
//                .orElse(Collections.emptyList());
//    }
//}
