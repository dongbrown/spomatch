package com.spomatch.repository;

import com.spomatch.entity.SportsFacilityProgram;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<SportsFacilityProgram, Long> {
}
