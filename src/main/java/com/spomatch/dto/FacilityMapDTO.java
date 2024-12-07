package com.spomatch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityMapDTO {
    private Long id;
    private String facilityName;        // 시설명
    private String programName;         // 프로그램명
    private Double latitude;            // 위도
    private Double longitude;           // 경도
    private String cityName;            // 시도명
    private String districtName;        // 시군구명
    private String address;             // 시설주소
    private String phoneNumber;         // 시설전화번호
    private String beginDate;           // 프로그램시작일자
    private String endDate;             // 프로그램종료일자
    private BigDecimal price;           // 프로그램가격
    private BigDecimal recruitNumber;   // 프로그램모집인원수
    private String homepageUrl;         // 홈페이지URL
    private String targetName;          // 프로그램대상명
    private String operationDays;       // 프로그램개설요일명
    private String operationTime;       // 프로그램개설시간대값
}