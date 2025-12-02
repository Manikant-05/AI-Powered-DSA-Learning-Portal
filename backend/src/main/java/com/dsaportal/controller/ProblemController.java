package com.dsaportal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dsaportal.dto.ProblemDto;
import com.dsaportal.entity.Problem;
import com.dsaportal.service.ProblemService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/problems")
public class ProblemController {
    
    @Autowired
    private ProblemService problemService;

    @GetMapping
    public ResponseEntity<List<ProblemDto>> getAllProblems(
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String search) {
        
        Problem.Difficulty diff = null;
        Problem.Topic top = null;
        
        if (difficulty != null) {
            try {
                diff = Problem.Difficulty.valueOf(difficulty.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        
        if (topic != null) {
            try {
                top = Problem.Topic.valueOf(topic.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        
        List<ProblemDto> problems = problemService.getProblemsByFilters(diff, top, search);
        return ResponseEntity.ok(problems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProblemDto> getProblemById(@PathVariable Long id) {
        Optional<ProblemDto> problem = problemService.getProblemById(id);
        return problem.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProblemDto> createProblem(@RequestBody Problem problem) {
        ProblemDto createdProblem = problemService.createProblem(problem);
        return ResponseEntity.ok(createdProblem);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProblemDto> updateProblem(@PathVariable Long id, @RequestBody Problem problem) {
        Optional<ProblemDto> updatedProblem = problemService.updateProblem(id, problem);
        return updatedProblem.map(ResponseEntity::ok)
                           .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProblem(@PathVariable Long id) {
        boolean deleted = problemService.deleteProblem(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/solved/{userId}")
    public ResponseEntity<List<ProblemDto>> getSolvedProblems(@PathVariable Long userId) {
        List<ProblemDto> problems = problemService.getSolvedProblems(userId);
        return ResponseEntity.ok(problems);
    }

    @GetMapping("/unsolved/{userId}")
    public ResponseEntity<List<ProblemDto>> getUnsolvedProblems(@PathVariable Long userId) {
        List<ProblemDto> problems = problemService.getUnsolvedProblems(userId);
        return ResponseEntity.ok(problems);
    }

    @GetMapping("/{id}/recommendations")
    public ResponseEntity<List<ProblemDto>> getRecommendations(@PathVariable Long id) {
        List<ProblemDto> recommendations = problemService.getRecommendations(id);
        return ResponseEntity.ok(recommendations);
    }
}
