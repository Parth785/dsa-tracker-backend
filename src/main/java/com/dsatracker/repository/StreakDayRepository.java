package com.dsatracker.repository;

import com.dsatracker.entity.StreakDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StreakDayRepository extends JpaRepository<StreakDay, Long> {

    List<StreakDay> findByUserIdOrderByDateDesc(Long userId);

    Optional<StreakDay> findByUserIdAndDate(Long userId, LocalDate date);

    List<StreakDay> findByUserIdAndDateBetweenOrderByDateDesc(Long userId, LocalDate start, LocalDate end);

    @Query("SELECT COUNT(s) FROM StreakDay s WHERE s.user.id = :userId AND s.status = 'done'")
    long countDoneDaysByUserId(@Param("userId") Long userId);
}
