package com.dsatracker.service;

import com.dsatracker.dto.ProblemDto;
import com.dsatracker.entity.Problem;
import com.dsatracker.entity.User;
import com.dsatracker.repository.ProblemRepository;
import com.dsatracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemService {

    @Autowired private ProblemRepository problemRepository;
    @Autowired private UserRepository userRepository;

    // ── Fetch logged-in user ──────────────────────────────────────
    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ── Map entity → response DTO ─────────────────────────────────
    private ProblemDto.Response toResponse(Problem p) {
        ProblemDto.Response r = new ProblemDto.Response();
        r.setId(p.getId());
        r.setName(p.getName());
        r.setLcNumber(p.getLcNumber());
        r.setPattern(p.getPattern());
        r.setDifficulty(p.getDifficulty());
        r.setType(p.getType());
        r.setTimeTaken(p.getTimeTaken());
        r.setTriggerNote(p.getTriggerNote());
        r.setMistakeNote(p.getMistakeNote());
        r.setRevisionStatus(p.getRevisionStatus());
        r.setDate(p.getDate());
        r.setCreatedAt(p.getCreatedAt());
        r.setUpdatedAt(p.getUpdatedAt());
        r.setSolutionCode(p.getSolutionCode());

        return r;
    }

    // ── GET all problems for user ─────────────────────────────────
    public List<ProblemDto.Response> getAllProblems(String email) {
        User user = getUser(email);
        return problemRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── GET single problem ────────────────────────────────────────
    public ProblemDto.Response getProblem(Long id, String email) {
        User user = getUser(email);
        Problem p = problemRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        return toResponse(p);
    }

    // ── CREATE problem ────────────────────────────────────────────
    public ProblemDto.Response createProblem(ProblemDto.CreateRequest request, String email) {
        User user = getUser(email);
        Problem p = Problem.builder()
                .user(user)
                .name(request.getName())
                .lcNumber(request.getLcNumber())
                .pattern(request.getPattern())
                .difficulty(request.getDifficulty() != null ? request.getDifficulty() : "Easy")
                .type(request.getType() != null ? request.getType() : "new")
                .timeTaken(request.getTimeTaken())
                .triggerNote(request.getTriggerNote())
                .mistakeNote(request.getMistakeNote())
                .revisionStatus(request.getRevisionStatus() != null ? request.getRevisionStatus() : "no")
                .date(request.getDate() != null ? request.getDate() : LocalDate.now())
                .solutionCode(request.getSolutionCode())
                .build();
        return toResponse(problemRepository.save(p));
    }

    // ── UPDATE problem ────────────────────────────────────────────
    public ProblemDto.Response updateProblem(Long id, ProblemDto.UpdateRequest request, String email) {
        User user = getUser(email);
        Problem p = problemRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Problem not found or access denied"));

        if (request.getName() != null)           p.setName(request.getName());
        if (request.getLcNumber() != null)        p.setLcNumber(request.getLcNumber());
        if (request.getPattern() != null)         p.setPattern(request.getPattern());
        if (request.getDifficulty() != null)      p.setDifficulty(request.getDifficulty());
        if (request.getType() != null)            p.setType(request.getType());
        if (request.getTimeTaken() != null)       p.setTimeTaken(request.getTimeTaken());
        if (request.getTriggerNote() != null)     p.setTriggerNote(request.getTriggerNote());
        if (request.getMistakeNote() != null)     p.setMistakeNote(request.getMistakeNote());
        if (request.getRevisionStatus() != null)  p.setRevisionStatus(request.getRevisionStatus());
        if (request.getDate() != null)            p.setDate(request.getDate());
        if (request.getSolutionCode() != null)    p.setSolutionCode(request.getSolutionCode());

        return toResponse(problemRepository.save(p));
    }

    // ── DELETE problem ────────────────────────────────────────────
    public void deleteProblem(Long id, String email) {
        User user = getUser(email);
        Problem p = problemRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Problem not found or access denied"));
        problemRepository.delete(p);
    }

    // ── GET pattern counts ────────────────────────────────────────
    public List<ProblemDto.PatternCount> getPatternCounts(String email) {
        User user = getUser(email);
        return problemRepository.countByPatternForUser(user.getId())
                .stream()
                .map(row -> new ProblemDto.PatternCount((String) row[0], (Long) row[1]))
                .collect(Collectors.toList());
    }

    // ── GET problems needing revision ─────────────────────────────
    public List<ProblemDto.Response> getRevisionProblems(String email) {
        User user = getUser(email);
        return problemRepository.findByUserIdAndRevisionStatusNotOrderByDateDesc(user.getId(), "no")
                .stream().map(this::toResponse).collect(Collectors.toList());
    }
}
