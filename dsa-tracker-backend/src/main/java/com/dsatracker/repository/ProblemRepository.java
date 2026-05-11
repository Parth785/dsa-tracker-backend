package com.dsatracker.repository;

import com.dsatracker.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    List<Problem> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Problem> findByUserIdAndPatternOrderByCreatedAtDesc(Long userId, String pattern);

    List<Problem> findByUserIdAndRevisionStatusNotOrderByDateDesc(Long userId, String status);

    Optional<Problem> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT COUNT(p) FROM Problem p WHERE p.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    @Query("SELECT p.pattern, COUNT(p) FROM Problem p WHERE p.user.id = :userId GROUP BY p.pattern")
    List<Object[]> countByPatternForUser(@Param("userId") Long userId);
}
