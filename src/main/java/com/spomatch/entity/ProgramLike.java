package com.spomatch.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "PROGRAM_LIKES")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    private SportsFacilityProgram program;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")  // MEMBER_ID와 매핑
    private Member member;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}