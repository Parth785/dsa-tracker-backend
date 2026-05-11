package com.dsatracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

public class StreakDto {

    @Data
    public static class MarkRequest {
        @NotNull(message = "Date is required")
        private LocalDate date;

        @NotBlank(message = "Status is required")
        private String status;  // done | skip | clear
    }

    @Data
    public static class DayResponse {
        private Long id;
        private LocalDate date;
        private String status;

        public DayResponse(Long id, LocalDate date, String status) {
            this.id = id;
            this.date = date;
            this.status = status;
        }
    }

    @Data
    public static class StatsResponse {
        private int currentStreak;
        private long totalDoneDays;
        private long totalProblems;
        private java.util.List<ProblemDto.PatternCount> patternCounts;
    }
}
