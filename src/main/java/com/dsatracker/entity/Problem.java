package com.dsatracker.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "problems")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(name = "lc_number")
    private String lcNumber;

    private String pattern;

    private String difficulty;   // Easy | Medium | Hard

    private String type;         // new | revision

    @Column(name = "time_taken")
    private Integer timeTaken;   // minutes

    @Column(name = "trigger_note", columnDefinition = "TEXT")
    private String triggerNote;

    @Column(name = "mistake_note", columnDefinition = "TEXT")
    private String mistakeNote;

    @Column(name = "revision_status")
    private String revisionStatus;  // no | yes | hard

    @Column(nullable = false)
    private LocalDate date;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
