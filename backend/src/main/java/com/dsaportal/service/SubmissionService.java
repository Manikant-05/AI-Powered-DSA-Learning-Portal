package com.dsaportal.service;

import com.dsaportal.dto.ProblemDto;
import com.dsaportal.dto.SubmissionDto;
import com.dsaportal.entity.Problem;
import com.dsaportal.entity.Submission;
import com.dsaportal.entity.User;
import com.dsaportal.repository.ProblemRepository;
import com.dsaportal.repository.SubmissionRepository;
import com.dsaportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubmissionService {
    
    @Autowired
    private SubmissionRepository submissionRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProblemRepository problemRepository;
    
    @Autowired
    private Judge0Service judge0Service;
    
    @Autowired
    private CodeAnalysisService codeAnalysisService;

    @Autowired
    private GeminiService geminiService;
    
    public List<SubmissionDto> getSubmissionsByUserId(Long userId) {
        return submissionRepository.findByUserIdOrderBySubmittedAtDesc(userId)
                .stream()
                .map(SubmissionDto::new)
                .collect(Collectors.toList());
    }
    
    public List<SubmissionDto> getSubmissionsByProblemId(Long problemId) {
        return submissionRepository.findByProblemId(problemId)
                .stream()
                .map(SubmissionDto::new)
                .collect(Collectors.toList());
    }
    
    public List<SubmissionDto> getSubmissionsByUserAndProblem(Long userId, Long problemId) {
        return submissionRepository.findByUserIdAndProblemId(userId, problemId)
                .stream()
                .map(SubmissionDto::new)
                .collect(Collectors.toList());
    }
    
    public Optional<SubmissionDto> getSubmissionById(Long id) {
        return submissionRepository.findById(id)
                .map(SubmissionDto::new);
    }
    
    public SubmissionDto createSubmission(Long userId, Long problemId, String code, Submission.Language language) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        
        Submission submission = new Submission(user, problem, code, language);
        submission.setStatus(Submission.Status.PENDING);
        
        // Submit to Judge0
        String judge0Token = judge0Service.submitCode(submission);
        if (judge0Token != null) {
            submission.setJudge0Token(judge0Token);
        }
        
        Submission savedSubmission = submissionRepository.save(submission);
        return new SubmissionDto(savedSubmission);
    }
    
    public SubmissionDto updateSubmissionResult(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        
        if (submission.getJudge0Token() != null) {
            Submission.Status status = judge0Service.getSubmissionResultWithPolling(submission.getJudge0Token());
            submission.setStatus(status);
            
            // Get code analysis from Gemini
            CodeAnalysisService.CodeAnalysisResult analysis = codeAnalysisService.analyzeCode(submission);
            
            // Calculate accuracy and other metrics based on Judge0 result and analysis
            if (status == Submission.Status.ACCEPTED) {
                submission.setAccuracy(100.0);
                submission.setTestCasesPassed(1);
                submission.setTotalTestCases(1);
            } else if (status == Submission.Status.COMPILATION_ERROR || status == Submission.Status.RUNTIME_ERROR) {
                // If there's a compilation or runtime error, check if it's due to syntax issues
                if (!analysis.isSyntaxCorrect()) {
                    submission.setAccuracy(0.0);
                } else {
                    // If syntax is correct but still error, give partial credit based on efficiency
                    submission.setAccuracy(Math.max(0.0, analysis.getEfficiencyScore() * 0.5));
                }
                submission.setTestCasesPassed(0);
                submission.setTotalTestCases(1);
            } else {
                // For other statuses (WRONG_ANSWER, TIME_LIMIT_EXCEEDED, etc.)
                submission.setAccuracy(Math.max(0.0, analysis.getEfficiencyScore() * 0.7));
                submission.setTestCasesPassed(0);
                submission.setTotalTestCases(1);
            }
            
            // Store analysis feedback
            submission.setAnalysisFeedback(analysis.getFeedback());
            submission.setEfficiencyScore(analysis.getEfficiencyScore());
            
            submissionRepository.save(submission);
        }
        
        return new SubmissionDto(submission);
    }

    public Optional<ProblemDto> getNextProblemSuggestion(Long userId, Long currentProblemId, Double score) {
        double normalizedScore = score != null ? score : 0.0;
        return problemRepository.findById(currentProblemId)
                .flatMap(problem -> geminiService
                        .suggestNextProblem(userId, problem, normalizedScore)
                        .map(ProblemDto::new));
    }
    
    public Long getTotalSubmissionsByUserId(Long userId) {
        return submissionRepository.countTotalSubmissionsByUserId(userId);
    }
    
    public Long getAcceptedSubmissionsByUserId(Long userId) {
        return submissionRepository.countAcceptedSubmissionsByUserId(userId);
    }
    
    public Double getAverageAccuracyByUserId(Long userId) {
        Double accuracy = submissionRepository.getAverageAccuracyByUserId(userId);
        return accuracy != null ? accuracy : 0.0;
    }
}
