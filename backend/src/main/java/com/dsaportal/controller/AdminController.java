package com.dsaportal.controller;

import com.dsaportal.dto.UserSummaryDto;
import com.dsaportal.entity.User;
import com.dsaportal.repository.UserRepository;
import com.dsaportal.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubmissionService submissionService;

    @GetMapping("/users")
    public ResponseEntity<List<UserSummaryDto>> getAllUsers() {
        List<User> users = userRepository.findAll();
        
        List<UserSummaryDto> userSummaries = users.stream().map(user -> {
            Long totalSubmissions = submissionService.getTotalSubmissionsByUserId(user.getId());
            Long solvedProblems = submissionService.getAcceptedSubmissionsByUserId(user.getId());
            Double averageAccuracy = submissionService.getAverageAccuracyByUserId(user.getId());
            
            return new UserSummaryDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                totalSubmissions,
                solvedProblems,
                averageAccuracy
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(userSummaries);
    }
}
