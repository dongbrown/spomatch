package com.spomatch.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "popular_programs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PopularPrograms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String programIds;

    private LocalDateTime createdAt;

    @Builder
    public PopularPrograms(String programIds, LocalDateTime createdAt) {
        this.programIds = programIds;
        this.createdAt = createdAt;
    }
}
