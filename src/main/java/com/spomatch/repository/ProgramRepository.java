package com.spomatch.repository;

import com.spomatch.entity.SportsFacilityProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramRepository extends JpaRepository<SportsFacilityProgram, Long> {
    List<SportsFacilityProgram> findByLatitudeIsNull();

}
