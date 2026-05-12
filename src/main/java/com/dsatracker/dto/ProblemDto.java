package com.dsatracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProblemDto {

    @Data
    public static class CreateRequest {
        @NotBlank(message = "Problem name is required")
        private String name;

        private String lcNumber;

        @NotBlank(message = "Pattern is required")
        private String pattern;

        private String difficulty;       // Easy | Medium | Hard
        private String type;             // new | revision
        private Integer timeTaken;
        private String triggerNote;
        private String mistakeNote;
        private String revisionStatus;   // no | yes | hard
        private LocalDate date;
        private String solutionCode;
    }

    @Data
    public static class UpdateRequest {
        private String name;
        private String lcNumber;
        private String pattern;
        private String difficulty;
        private String type;
        private Integer timeTaken;
        private String triggerNote;
        private String mistakeNote;
        private String revisionStatus;
        private LocalDate date;
        private String solutionCode;
    }

    @Data
    public static class Response {
        private Long id;
        private String name;
        private String lcNumber;
        private String pattern;
        private String difficulty;
        private String type;
        private Integer timeTaken;
        private String triggerNote;
        private String mistakeNote;
        private String revisionStatus;
        private LocalDate date;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String solutionCode;
    }

    @Data
    public static class PatternCount {
        private String pattern;
        private Long count;

        public PatternCount(String pattern, Long count) {
            this.pattern = pattern;
            this.count = count;
        }
    }
}
