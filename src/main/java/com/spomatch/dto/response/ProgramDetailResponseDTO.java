package com.spomatch.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgramDetailResponseDTO {
    // 프로그램 식별 정보
    private Long id;
    private String programName;

    // 시설 정보
    private FacilityInfo facility;

    // 수업 정보
    private ClassInfo classInfo;

    @Getter @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FacilityInfo {
        private String facilityName;              // 시설명
        private String facilityType;              // 시설유형명
        private String address;                   // 시설주소
        private String contact;                   // 시설전화번호
        private String homepage;                  // 홈페이지URL
        private String safetyManagement;          // 안전조치내용
    }

    @Getter @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClassInfo {
        // 프로그램 기본 정보
        private String programType;               // 프로그램유형명
        private String programName;               // 프로그램명
        private String targetName;                 // 프로그램대상명
        private List<String> weekdays;            // 프로그램개설요일명
        private String time;                      // 프로그램개설시간대값
        private String startDate;                 // 프로그램시작일자
        private String endDate;                   // 프로그램종료일자

        // 모집/가격 정보
        private Integer price;                    // 프로그램가격
        private String priceType;                 // 프로그램가격유형명
        private Integer recruitNumber;            // 프로그램모집인원수
        private Integer currentParticipants;      // 현재 참여 인원수

        // 교육 관련 정보
        private String educationGoal;             // 교육목표내용
        private String leaderQualification;       // 지도자자격내용
        private String protectorParticipation;    // 보호인원참가여부
    }
}