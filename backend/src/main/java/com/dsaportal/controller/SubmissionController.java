package com.dsaportal.controller;

import com.dsaportal.dto.ProblemDto;
import com.dsaportal.dto.SubmissionDto;
import com.dsaportal.entity.Submission;
import com.dsaportal.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/submissions")
public class SubmissionController {
    
    @Autowired
    private SubmissionService submissionService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubmissionDto>> getSubmissionsByUserId(@PathVariable Long userId) {
        List<SubmissionDto> submissions = submissionService.getSubmissionsByUserId(userId);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/problem/{problemId}")
    public ResponseEntity<List<SubmissionDto>> getSubmissionsByProblemId(@PathVariable Long problemId) {
        List<SubmissionDto> submissions = submissionService.getSubmissionsByProblemId(problemId);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/user/{userId}/problem/{problemId}")
    public ResponseEntity<List<SubmissionDto>> getSubmissionsByUserAndProblem(
            @PathVariable Long userId, @PathVariable Long problemId) {
        List<SubmissionDto> submissions = submissionService.getSubmissionsByUserAndProblem(userId, problemId);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubmissionDto> getSubmissionById(@PathVariable Long id) {
        Optional<SubmissionDto> submission = submissionService.getSubmissionById(id);
        return submission.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SubmissionDto> createSubmission(
            @RequestParam Long userId,
            @RequestParam Long problemId,
            @RequestParam String code,
            @RequestParam String language) {
        
        try {
            // Convert frontend language names to backend enum values
            String upperLanguage = language.toUpperCase();
            Submission.Language lang;
            
            switch (upperLanguage) {
                case "PYTHON":
                    lang = Submission.Language.PYTHON;
                    break;
                case "JAVA":
                    lang = Submission.Language.JAVA;
                    break;
                case "CPP":
                    lang = Submission.Language.CPP;
                    break;
                case "JAVASCRIPT":
                    lang = Submission.Language.JAVASCRIPT;
                    break;
                case "C":
                    lang = Submission.Language.C;
                    break;
                default:
                    return ResponseEntity.badRequest().build();
            }
            
            SubmissionDto submission = submissionService.createSubmission(userId, problemId, code, lang);
            return ResponseEntity.ok(submission);
        } catch (Exception e) {
            System.err.println("Error in createSubmission: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/submit-and-evaluate")
    public ResponseEntity<SubmissionDto> submitAndEvaluate(
            @RequestParam Long userId,
            @RequestParam Long problemId,
            @RequestParam String code,
            @RequestParam String language) {
        
        try {
            // Convert frontend language names to backend enum values
            String upperLanguage = language.toUpperCase();
            Submission.Language lang;
            
            switch (upperLanguage) {
                case "PYTHON":
                    lang = Submission.Language.PYTHON;
                    break;
                case "JAVA":
                    lang = Submission.Language.JAVA;
                    break;
                case "CPP":
                    lang = Submission.Language.CPP;
                    break;
                case "JAVASCRIPT":
                    lang = Submission.Language.JAVASCRIPT;
                    break;
                case "C":
                    lang = Submission.Language.C;
                    break;
                default:
                    return ResponseEntity.badRequest().build();
            }
            
            SubmissionDto submission = submissionService.createSubmission(userId, problemId, code, lang);
            
            // Immediately evaluate the submission
            SubmissionDto evaluatedSubmission = submissionService.updateSubmissionResult(submission.getId());
            return ResponseEntity.ok(evaluatedSubmission);
        } catch (Exception e) {
            System.err.println("Error in submit-and-evaluate: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/result")
    public ResponseEntity<SubmissionDto> updateSubmissionResult(@PathVariable Long id) {
        SubmissionDto submission = submissionService.updateSubmissionResult(id);
        return ResponseEntity.ok(submission);
    }

    @GetMapping("/next-problem")
    public ResponseEntity<ProblemDto> getNextProblem(
            @RequestParam Long userId,
            @RequestParam Long currentProblemId,
            @RequestParam(required = false) Double score) {
        return submissionService.getNextProblemSuggestion(userId, currentProblemId, score)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stats/user/{userId}")
    public ResponseEntity<Object> getUserStats(@PathVariable Long userId) {
        Long totalSubmissions = submissionService.getTotalSubmissionsByUserId(userId);
        Long acceptedSubmissions = submissionService.getAcceptedSubmissionsByUserId(userId);
        Double averageAccuracy = submissionService.getAverageAccuracyByUserId(userId);
        
        return ResponseEntity.ok(new UserStatsResponse(totalSubmissions, acceptedSubmissions, averageAccuracy));
    }
    
    // Inner class for response
    public static class UserStatsResponse {
        public final Long totalSubmissions;
        public final Long acceptedSubmissions;
        public final Double averageAccuracy;
        
        public UserStatsResponse(Long totalSubmissions, Long acceptedSubmissions, Double averageAccuracy) {
            this.totalSubmissions = totalSubmissions;
            this.acceptedSubmissions = acceptedSubmissions;
            this.averageAccuracy = averageAccuracy;
        }
    }
}
