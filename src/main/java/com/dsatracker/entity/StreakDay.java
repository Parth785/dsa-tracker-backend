package com.dsatracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "streak_days",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StreakDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String status;  // done | skip
}
