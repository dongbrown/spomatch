package com.spomatch.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "PROGRAM_REGISTERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    private SportsFacilityProgram program;

    @Column(name = "user_id", length = 100)
    private String userId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
