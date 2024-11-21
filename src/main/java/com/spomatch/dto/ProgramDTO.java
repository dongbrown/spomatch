package com.spomatch.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgramDTO {
    private Long id;
    private String name;
    private String facility;
    private String region;
    private String sportType;
    private String ageType;
    private List<String> weekdays;
    private Integer price;
    private LocalDate startDate;
    private LocalDate endDate;
    private String time;
    private Integer currentParticipants;
    private Integer maxParticipants;
    private Long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
