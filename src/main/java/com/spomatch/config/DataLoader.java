package com.spomatch.config;

import com.spomatch.service.ProgramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final ProgramService programService;  // 인터페이스 사용

    @Override
    public void run(String... args) {
        try {
            log.info("Starting data import...");
            String filePath = "src/main/resources/data/publicProgram.json";
            long beforeTime = System.currentTimeMillis();
            programService.importJsonData(filePath);
            log.info("Data import completed.");

            log.info("Starting coordinates update...");
            programService.updateCoordinates();
            long afterTime = System.currentTimeMillis();
            long secDiffTime = (afterTime - beforeTime)/1000;
            log.info("Coordinates update completed. 소요시간 : {}", secDiffTime);
        } catch (Exception e) {
            log.error("Failed to process data", e);
        }
    }
}