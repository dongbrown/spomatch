package com.spomatch.service;

import com.spomatch.dto.ProgramDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class ProgramLikeServiceTest {

    @Autowired
    private ProgramLikeService programLikeService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testGetTopLikedPrograms() {
        // 1. Redis에 임의의 인기 프로그램 데이터 저장
        String topPrograms = "1,2,3"; // 프로그램 ID들
        redisTemplate.opsForValue().set("top:liked:programs", topPrograms);

        // 2. 실제 서비스 메서드 호출
        List<ProgramDTO> topLikedPrograms = programLikeService.getTopLikedPrograms();

        // 3. 검증: 반환된 프로그램 리스트의 크기 및 내용 확인
        Assertions.assertNotNull(topLikedPrograms);
        Assertions.assertFalse(topLikedPrograms.isEmpty());
        Assertions.assertEquals(3, topLikedPrograms.size()); // 예상: 3개의 프로그램

        List<Long> expectedProgramIds = Arrays.asList(1L, 2L, 3L);
        List<Long> actualProgramIds = topLikedPrograms.stream()
                .map(ProgramDTO::getId) // ProgramDTO에서 프로그램 ID 추출
                .toList();

        Assertions.assertEquals(expectedProgramIds, actualProgramIds);

        // 4. 반환된 데이터 출력 (선택 사항)
        topLikedPrograms.forEach(program -> {
            System.out.println("Program ID: " + program.getId());
            System.out.println("Program Name: " + program.getProgramName());
            System.out.println("Facility Name: " + program.getFacilityName());
        });
    }
}
