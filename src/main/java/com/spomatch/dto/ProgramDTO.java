package com.spomatch.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgramDTO {
    private Long id;
    private String facilityName;              // 시설명
    private String facilityTypeName;          // 시설유형명
    private String cityCode;                  // 시도코드
    private String cityName;                  // 시도명
    private String districtCode;              // 시군구코드
    private String districtName;              // 시군구명
    private String facilityAddress;           // 시설주소
    private String facilityPhoneNumber;       // 시설전화번호
    private String programTypeName;           // 프로그램유형명
    private String programName;               // 프로그램명
    private String programTargetName;         // 프로그램대상명
    private String programBeginDate;          // 프로그램시작일자
    private String programEndDate;            // 프로그램종료일자
    private String programOperationDays;      // 프로그램개설요일명
    private String programOperationTime;      // 프로그램개설시간대값
    private int programRecruitNumber;         // 프로그램모집인원수
    private int programPrice;                 // 프로그램가격
    private String programPriceTypeName;      // 프로그램가격유형명
    private String homepageUrl;               // 홈페이지URL
    private String safetyManagementContent;   // 안전조치내용
    private String educationGoalContent;      // 교육목표내용
    private String protectorParticipationYn;  // 보호인원참가여부
    private String leaderQualificationContent;// 지도자자격내용
    private String event; // 종목
}