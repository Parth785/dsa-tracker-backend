package com.dsatracker.service;

import com.dsatracker.dto.ProblemDto;
import com.dsatracker.dto.StreakDto;
import com.dsatracker.entity.StreakDay;
import com.dsatracker.entity.User;
import com.dsatracker.repository.ProblemRepository;
import com.dsatracker.repository.StreakDayRepository;
import com.dsatracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StreakService {

    @Autowired private StreakDayRepository streakDayRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProblemRepository problemRepository;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ── Mark or update a day ──────────────────────────────────────
    public StreakDto.DayResponse markDay(StreakDto.MarkRequest request, String email) {
        User user = getUser(email);

        // "clear" removes the entry
        if ("clear".equals(request.getStatus())) {
            streakDayRepository.findByUserIdAndDate(user.getId(), request.getDate())
                    .ifPresent(streakDayRepository::delete);
            return new StreakDto.DayResponse(null, request.getDate(), "cleared");
        }

        Optional<StreakDay> existing = streakDayRepository.findByUserIdAndDate(user.getId(), request.getDate());
        StreakDay day;
        if (existing.isPresent()) {
            day = existing.get();
            day.setStatus(request.getStatus());
        } else {
            day = StreakDay.builder()
                    .user(user)
                    .date(request.getDate())
                    .status(request.getStatus())
                    .build();
        }
        streakDayRepository.save(day);
        return new StreakDto.DayResponse(day.getId(), day.getDate(), day.getStatus());
    }

    // ── Get calendar (last 90 days) ───────────────────────────────
    public List<StreakDto.DayResponse> getCalendar(String email) {
        User user = getUser(email);
        LocalDate from = LocalDate.now().minusDays(90);
        LocalDate to = LocalDate.now().plusDays(30);
        return streakDayRepository.findByUserIdAndDateBetweenOrderByDateDesc(user.getId(), from, to)
                .stream()
                .map(d -> new StreakDto.DayResponse(d.getId(), d.getDate(), d.getStatus()))
                .collect(Collectors.toList());
    }

    // ── Calculate current streak ──────────────────────────────────
    public int calculateStreak(String email) {
        User user = getUser(email);
        List<StreakDay> days = streakDayRepository.findByUserIdOrderByDateDesc(user.getId());

        int streak = 0;
        LocalDate cursor = LocalDate.now();

        for (StreakDay day : days) {
            if (day.getDate().equals(cursor) || day.getDate().equals(cursor.minusDays(1))) {
                if ("done".equals(day.getStatus())) {
                    streak++;
                    cursor = day.getDate().minusDays(1);
                } else if ("skip".equals(day.getStatus())) {
                    cursor = day.getDate().minusDays(1);
                    // skip days don't break streak but don't add to it
                } else {
                    break;
                }
            } else if (day.getDate().isBefore(cursor)) {
                break;
            }
        }
        return streak;
    }

    // ── Full stats response ───────────────────────────────────────
    public StreakDto.StatsResponse getStats(String email) {
        User user = getUser(email);

        StreakDto.StatsResponse stats = new StreakDto.StatsResponse();
        stats.setCurrentStreak(calculateStreak(email));
        stats.setTotalDoneDays(streakDayRepository.countDoneDaysByUserId(user.getId()));
        stats.setTotalProblems(problemRepository.countByUserId(user.getId()));
        stats.setPatternCounts(
            problemRepository.countByPatternForUser(user.getId())
                .stream()
                .map(row -> new ProblemDto.PatternCount((String) row[0], (Long) row[1]))
                .collect(Collectors.toList())
        );
        return stats;
    }
}
