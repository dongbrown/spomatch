package com.spomatch.service;

import com.spomatch.dao.ProgramDAO;
import com.spomatch.dto.ProgramDTO;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgramLikeServiceImpl implements ProgramLikeService {
    private final ProgramDAO programDAO;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String RECENT_LIKES_KEY = "recent:likes";
    private static final String RECOMMENDATION_LIST_KEY = "recommendation:list";
    private static final int MAX_SAMPLE_SIZE = 600;
    private static final int MAX_RECOMMENDATION_SIZE = 100;

    @Override
    @Transactional
    public boolean toggleLike(Long programId, Long memberId) {
        boolean isLiked = checkLikeStatus(programId, memberId);
        if (isLiked) {
            programDAO.deleteProgramLike(programId, memberId);
        } else {
            programDAO.insertProgramLike(programId, memberId);
            addRecentLike(programId);
        }
        return !isLiked;
    }

    @Override
    public void addRecentLike(Long programId) {
        ListOperations<String, String> listOps = redisTemplate.opsForList();
        listOps.rightPush(RECENT_LIKES_KEY, programId.toString());

        Long size = listOps.size(RECENT_LIKES_KEY);
        if (size != null && size > MAX_SAMPLE_SIZE) {
            listOps.leftPop(RECENT_LIKES_KEY);
        }
        log.info("Added recent like for program: {}", programId);
    }

    @Override
    @Transactional
    public void unlike(Long programId, Long memberId) {
        boolean isLiked = checkLikeStatus(programId, memberId);

        if (isLiked) {
            programDAO.deleteProgramLike(programId, memberId);
            log.info("Program unliked - programId: {}, memberId: {}", programId, memberId);
        } else {
            log.warn("Program was not liked - programId: {}, memberId: {}", programId, memberId);
        }
    }

    @Override
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void updateTopLikedPrograms() {
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            List<String> recentLikesStr = listOps.range(RECENT_LIKES_KEY, 0, -1);

            if (recentLikesStr == null || recentLikesStr.isEmpty()) {
                return;
            }

            List<Long> recentLikes = recentLikesStr.stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            Map<Long, Long> frequencyMap = recentLikes.stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            List<Long> topPrograms = frequencyMap.entrySet().stream()
                    .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                    .limit(MAX_RECOMMENDATION_SIZE)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            // Redis 업데이트
            ListOperations<String, String> recommendationOps = redisTemplate.opsForList();
            redisTemplate.delete(RECOMMENDATION_LIST_KEY);
            topPrograms.forEach(programId ->
                    recommendationOps.rightPush(RECOMMENDATION_LIST_KEY, programId.toString())
            );

            // DB 업데이트
            List<Map<String, Object>> recommendations = new ArrayList<>();
            for (int i = 0; i < topPrograms.size(); i++) {
                Map<String, Object> params = new HashMap<>();
                params.put("programId", topPrograms.get(i));
                params.put("rank", i + 1);
                recommendations.add(params);
            }

            programDAO.deleteAllRecommendations();
            if (!recommendations.isEmpty()) {
                programDAO.insertRecommendations(recommendations);
            }

            log.info("Updated top liked programs in both Redis and DB. Total programs: {}", topPrograms.size());
        } catch (Exception e) {
            log.error("Error updating top liked programs", e);
            throw e;
        }
    }

    @Override
    public List<ProgramDTO> getTopLikedPrograms() {
        try {
            // 먼저 Redis에서 조회 시도
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            List<String> recommendationList = listOps.range(RECOMMENDATION_LIST_KEY, 0, -1);

            // Redis에 데이터가 없으면 DB에서 조회
            if (recommendationList == null || recommendationList.isEmpty()) {
                List<Long> dbRecommendations = programDAO.selectRecommendationList();
                if (dbRecommendations.isEmpty()) {
                    return Collections.emptyList();
                }
                return dbRecommendations.stream()
                        .map(programDAO::selectProgramDetail)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }

            // Redis 데이터 반환
            return recommendationList.stream()
                    .map(Long::parseLong)
                    .map(programDAO::selectProgramDetail)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting top liked programs", e);
            return Collections.emptyList();
        }
    }

    @Override
    public boolean checkLikeStatus(Long programId, Long memberId) {
        return programDAO.checkLikeStatus(programId, memberId);
    }

    @Override
    public List<ProgramDTO> getLikedPrograms(Long memberId) {
        return programDAO.selectLikedProgramList(memberId);
    }
}