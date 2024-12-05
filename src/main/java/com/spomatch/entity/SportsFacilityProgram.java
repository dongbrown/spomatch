package com.spomatch.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FACILITY_PROGRAM")
@Getter @Setter
@NoArgsConstructor
public class SportsFacilityProgram {

    @Id
    @JsonProperty("PROGRM_ID")  // 추가

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("FCLTY_NM")
    @Column(name = "FCLTY_NM", length = 200)
    private String facilityName;                  // 시설명

    @JsonProperty("FCLTY_TY_NM")
    @Column(name = "FCLTY_TY_NM", length = 200)
    private String facilityTypeName;              // 시설유형명

    @JsonProperty("CTPRVN_CD")
    @Column(name = "CTPRVN_CD", length = 30)
    private String cityCode;                      // 시도코드

    @JsonProperty("CTPRVN_NM")
    @Column(name = "CTPRVN_NM", length = 200)
    private String cityName;                      // 시도명

    @JsonProperty("SIGNGU_CD")
    @Column(name = "SIGNGU_CD", length = 30)
    private String districtCode;                  // 시군구코드

    @JsonProperty("SIGNGU_NM")
    @Column(name = "SIGNGU_NM", length = 200)
    private String districtName;                  // 시군구명

    @JsonProperty("FCLTY_ADDR")
    @Column(name = "FCLTY_ADDR", length = 200)
    private String facilityAddress;               // 시설주소

    @JsonProperty("FCLTY_TEL_NO")
    @Column(name = "FCLTY_TEL_NO", length = 20)
    private String facilityPhoneNumber;           // 시설전화번호

    @JsonProperty("PROGRM_TY_NM")
    @Column(name = "PROGRM_TY_NM", length = 200)
    private String programTypeName;               // 프로그램유형명

    @JsonProperty("PROGRM_NM")
    @Column(name = "PROGRM_NM", length = 200)
    private String programName;                   // 프로그램명

    @JsonProperty("PROGRM_TRGET_NM")
    @Column(name = "PROGRM_TRGET_NM", length = 200)
    private String programTargetName;             // 프로그램대상명

    @JsonProperty("PROGRM_BEGIN_DE")
    @Column(name = "PROGRM_BEGIN_DE", length = 8)
    private String programBeginDate;              // 프로그램시작일자

    @JsonProperty("PROGRM_END_DE")
    @Column(name = "PROGRM_END_DE", length = 8)
    private String programEndDate;                // 프로그램종료일자

    @JsonProperty("PROGRM_ESTBL_WKDAY_NM")
    @Column(name = "PROGRM_ESTBL_WKDAY_NM", length = 200)
    private String programOperationDays;          // 프로그램개설요일명

    @JsonProperty("PROGRM_ESTBL_TIZN_VALUE")
    @Column(name = "PROGRM_ESTBL_TIZN_VALUE", length = 200)
    private String programOperationTime;          // 프로그램개설시간대값

    @JsonProperty("PROGRM_RCRIT_NMPR_CO")
    @Column(name = "PROGRM_RCRIT_NMPR_CO", precision = 38)
    private BigDecimal programRecruitNumber;      // 프로그램모집인원수

    @JsonProperty("PROGRM_PRC")
    @Column(name = "PROGRM_PRC", precision = 28, scale = 5)
    private BigDecimal programPrice;              // 프로그램가격

    @JsonProperty("PROGRM_PRC_TY_NM")
    @Column(name = "PROGRM_PRC_TY_NM", length = 200)
    private String programPriceTypeName;          // 프로그램가격유형명

    @JsonProperty("HMPG_URL")
    @Column(name = "HMPG_URL", length = 500)
    private String homepageUrl;                   // 홈페이지URL

    @JsonProperty("SAFE_MANAGT_CN")
    @Column(name = "SAFE_MANAGT_CN", length = 4000)
    private String safetyManagementContent;       // 안전조치내용

    @JsonProperty("EDC_GOAL_CN")
    @Column(name = "EDC_GOAL_CN", length = 4000)
    private String educationGoalContent;          // 교육목표내용

    @JsonProperty("PRTC_NMPR_PARTCPT_AT")
    @Column(name = "PRTC_NMPR_PARTCPT_AT", length = 1)
    private String protectorParticipationYn;      // 보호인원참가여부

    @JsonProperty("LDR_QUALFC_CN")
    @Column(name = "LDR_QUALFC_CN", length = 1000)
    private String leaderQualificationContent;    // 지도자자격내용

    @Column(name = "view_count")
    private int viewCount = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 위도, 경도 추가
    private Double latitude;
    private Double longitude;


    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramRegister> registers = new ArrayList<>();

    // 연관관계 편의 메서드
    public void addLike(ProgramLike like) {
        likes.add(like);
        like.setProgram(this);
    }

    public void addRegister(ProgramRegister register) {
        registers.add(register);
        register.setProgram(this);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}