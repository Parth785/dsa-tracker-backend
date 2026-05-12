package com.dsatracker.controller;

import com.dsatracker.dto.StreakDto;
import com.dsatracker.service.StreakService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/streak")
public class StreakController {

    @Autowired private StreakService streakService;

    // GET calendar days
    @GetMapping("/calendar")
    public ResponseEntity<List<StreakDto.DayResponse>> getCalendar(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(streakService.getCalendar(userDetails.getUsername()));
    }

    // POST mark a day
    @PostMapping("/mark")
    public ResponseEntity<?> markDay(@Valid @RequestBody StreakDto.MarkRequest request,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        try {
            StreakDto.DayResponse response = streakService.markDay(request, userDetails.getUsername());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    // GET current streak count
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentStreak(@AuthenticationPrincipal UserDetails userDetails) {
        int streak = streakService.calculateStreak(userDetails.getUsername());
        return ResponseEntity.ok(new StreakResponse(streak));
    }

    // GET full stats (streak + totals + pattern counts)
    @GetMapping("/stats")
    public ResponseEntity<StreakDto.StatsResponse> getStats(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(streakService.getStats(userDetails.getUsername()));
    }

    record ErrorResponse(String message) {}
    record StreakResponse(int streak) {}
}
