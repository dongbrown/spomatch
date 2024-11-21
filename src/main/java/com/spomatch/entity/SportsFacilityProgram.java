package com.spomatch.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "FACILITY_PROGRAM")
@Getter
@Setter
@NoArgsConstructor
public class SportsFacilityProgram {

    @Id
    @Column(name = "FCLTY_NM", length = 200, nullable = false)
    private String facilityName;                  // 시설명

    @Column(name = "FCLTY_TY_NM", length = 200, nullable = false)
    private String facilityTypeName;              // 시설유형명

    @Column(name = "CTPRVN_CD", length = 30, nullable = false)
    private String cityCode;                      // 시도코드

    @Column(name = "CTPRVN_NM", length = 200, nullable = false)
    private String cityName;                      // 시도명

    @Column(name = "SIGNGU_CD", length = 30, nullable = false)
    private String districtCode;                  // 시군구코드

    @Column(name = "SIGNGU_NM", length = 200, nullable = false)
    private String districtName;                  // 시군구명

    @Column(name = "FCLTY_ADDR", length = 200, nullable = false)
    private String facilityAddress;               // 시설주소

    @Column(name = "FCLTY_TEL_NO", length = 20, nullable = false)
    private String facilityPhoneNumber;           // 시설전화번호

    @Column(name = "PROGRM_TY_NM", length = 200, nullable = false)
    private String programTypeName;               // 프로그램유형명

    @Column(name = "PROGRM_NM", length = 200, nullable = false)
    private String programName;                   // 프로그램명

    @Column(name = "PROGRM_TRGET_NM", length = 200, nullable = false)
    private String programTargetName;             // 프로그램대상명

    @Column(name = "PROGRM_BEGIN_DE", length = 8, nullable = false)
    private String programBeginDate;              // 프로그램시작일자

    @Column(name = "PROGRM_END_DE", length = 8, nullable = false)
    private String programEndDate;                // 프로그램종료일자

    @Column(name = "PROGRM_ESTBL_WKDAY_NM", length = 200, nullable = false)
    private String programOperationDays;          // 프로그램개설요일명

    @Column(name = "PROGRM_ESTBL_TIZN_VALUE", length = 200, nullable = false)
    private String programOperationTime;          // 프로그램개설시간대값

    @Column(name = "PROGRM_RCRIT_NMPR_CO", precision = 38, nullable = false)
    private int programRecruitNumber;      // 프로그램모집인원수

    @Column(name = "PROGRM_PRC", precision = 28, scale = 5, nullable = false)
    private int programPrice;              // 프로그램가격

    @Column(name = "PROGRM_PRC_TY_NM", length = 200, nullable = false)
    private String programPriceTypeName;          // 프로그램가격유형명

    @Column(name = "HMPG_URL", length = 500)    // URL은 nullable 허용
    private String homepageUrl;                   // 홈페이지URL

    // 청소년/유아동 관련 추가 정보 (nullable)
    @Column(name = "SAFE_MANAGT_CN", length = 4000)
    private String safetyManagementContent;       // 안전조치내용

    @Column(name = "EDC_GOAL_CN", length = 4000)
    private String educationGoalContent;          // 교육목표내용

    @Column(name = "PRTC_NMPR_PARTCPT_AT", length = 1)
    private String protectorParticipationYn;      // 보호인원참가여부

    @Column(name = "LDR_QUALFC_CN", length = 1000)
    private String leaderQualificationContent;    // 지도자자격내용
}