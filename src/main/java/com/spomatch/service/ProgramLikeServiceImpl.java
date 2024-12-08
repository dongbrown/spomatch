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
    private static final String TOP_PROGRAMS_KEY = "top:liked:programs";
    private static final int MAX_LIST_SIZE = 1000;

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
    @Scheduled(fixedRate = 6000)
    public void addRecentLike(Long programId) {
        ListOperations<String, String> listOps = redisTemplate.opsForList();
        listOps.rightPush(RECENT_LIKES_KEY, programId.toString());

        Long size = listOps.size(RECENT_LIKES_KEY);
        if (size != null && size > MAX_LIST_SIZE) {
            listOps.leftPop(RECENT_LIKES_KEY);
        }
        log.info("Added recent like for program: {}", programId);
    }

    @Override
    @Transactional
    public void unlike(Long programId, Long memberId) {
        // 찜 상태 확인
        boolean isLiked = checkLikeStatus(programId, memberId);

        if (isLiked) {
            // DB에서 찜 정보 삭제
            programDAO.deleteProgramLike(programId, memberId);

            // Redis에서 최근 찜 목록 업데이트
//            removeFromRecentLikes(programId);

            // Redis의 인기 프로그램 스코어 감소
//            updateTopPrograms(programId, -1);

            log.info("Program unliked - programId: {}, memberId: {}", programId, memberId);
        } else {
            log.warn("Program was not liked - programId: {}, memberId: {}", programId, memberId);
        }
    }

    @Override
    @Scheduled(cron = "0 0 * * * *")
    public void updateTopLikedPrograms() {
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
                .limit(100)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        redisTemplate.opsForValue().set(TOP_PROGRAMS_KEY,
                topPrograms.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")));
    }

    @Override
    public List<ProgramDTO> getTopLikedPrograms() {
        String topProgramsStr = redisTemplate.opsForValue().get(TOP_PROGRAMS_KEY);
        if (topProgramsStr == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(topProgramsStr.split(","))
                .map(Long::parseLong)
                .map(programDAO::selectProgramDetail)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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