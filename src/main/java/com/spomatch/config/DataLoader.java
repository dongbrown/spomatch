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
            // JSON 파일의 실제 경로를 지정해주세요
            String filePath = "src/main/resources/data/publicProgram.json";
            programService.importJsonData(filePath);
            log.info("Data import completed. Final progress: {}%",
                    String.format("%.2f", programService.getProgress()));
        } catch (Exception e) {
            log.error("Failed to import data", e);
        }
    }
}