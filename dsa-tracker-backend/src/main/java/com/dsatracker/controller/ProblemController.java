package com.dsatracker.controller;

import com.dsatracker.dto.ProblemDto;
import com.dsatracker.service.ProblemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    @Autowired private ProblemService problemService;

    // GET all problems
    @GetMapping
    public ResponseEntity<List<ProblemDto.Response>> getAll(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(problemService.getAllProblems(userDetails.getUsername()));
    }

    // GET single problem
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(problemService.getProblem(id, userDetails.getUsername()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST create
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ProblemDto.CreateRequest request,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        try {
            ProblemDto.Response created = problemService.createProblem(request, userDetails.getUsername());
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    // PUT update
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody ProblemDto.UpdateRequest request,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        try {
            ProblemDto.Response updated = problemService.updateProblem(id, request, userDetails.getUsername());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        try {
            problemService.deleteProblem(id, userDetails.getUsername());
            return ResponseEntity.ok(new SuccessResponse("Problem deleted"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    // GET pattern counts
    @GetMapping("/patterns")
    public ResponseEntity<List<ProblemDto.PatternCount>> getPatternCounts(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(problemService.getPatternCounts(userDetails.getUsername()));
    }

    // GET revision problems
    @GetMapping("/revision")
    public ResponseEntity<List<ProblemDto.Response>> getRevision(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(problemService.getRevisionProblems(userDetails.getUsername()));
    }

    record ErrorResponse(String message) {}
    record SuccessResponse(String message) {}
}
