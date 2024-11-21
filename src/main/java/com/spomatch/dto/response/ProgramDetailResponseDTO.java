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
    // 프로그램 기본 정보
    private Long id;
    private String name;
    private String description;    // 프로그램 상세 설명

    // 시설 정보
    private FacilityInfo facility;

    // 수업 정보
    private ClassInfo classInfo;

    // 강사 정보
    private InstructorInfo instructor;

    // 위치 정보
    private LocationInfo location;

    // 통계 정보
    private StatisticsInfo statistics;

    // 리뷰 정보
    private List<ReviewInfo> reviews;

    // 중첩 클래스들
    @Getter @Setter
    @Builder
    public static class FacilityInfo {
        private String facilityName;
        private String facilityType;
        private String contact;
        private String homepage;
        private List<String> amenities;    // 편의시설
        private List<String> images;       // 시설 이미지
    }

    @Getter @Setter
    @Builder
    public static class ClassInfo {
        private String sportType;
        private String ageType;
        private List<String> weekdays;
        private String time;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer price;
        private Integer currentParticipants;
        private Integer maxParticipants;
        private String level;              // 난이도
        private List<String> requirements; // 준비물
        private String curriculum;         // 커리큘럼
    }

    @Getter @Setter
    @Builder
    public static class InstructorInfo {
        private Long instructorId;
        private String name;
        private String profile;
        private String career;         // 경력
        private List<String> certificates; // 자격증
        private String introduction;   // 강사 소개
    }

    @Getter @Setter
    @Builder
    public static class LocationInfo {
        private String address;
        private String detailAddress;
        private Double latitude;       // 위도
        private Double longitude;      // 경도
        private List<TransportInfo> transports;  // 교통편 정보
    }

    @Getter @Setter
    @Builder
    public static class TransportInfo {
        private String type;          // 지하철/버스 등
        private String name;          // 역명/정류장명
        private String detail;        // 세부 정보
        private Integer distance;      // 도보 거리(m)
        private Integer duration;      // 도보 시간(분)
    }

    @Getter @Setter
    @Builder
    public static class StatisticsInfo {
        private Long viewCount;       // 조회수
        private Long likeCount;       // 찜하기 수
        private Double rating;        // 평균 평점
        private Integer reviewCount;  // 리뷰 수
        private Integer registerCount; // 수강 신청 수
        private Map<String, Integer> ageDistribution; // 연령대별 분포
    }

    @Getter
    @Setter
    @Builder
    public static class ReviewInfo {
        private Long reviewId;
        private String userName;
        private Integer rating;
        private String content;
        private LocalDateTime createdAt;
        private List<String> images;  // 리뷰 이미지
    }
}