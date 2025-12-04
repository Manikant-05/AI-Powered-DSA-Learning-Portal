package com.dsaportal.dto;

public class UserSummaryDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    private Long totalSubmissions;
    private Long solvedProblems;
    private Double averageAccuracy;

    public UserSummaryDto(Long id, String username, String email, String role, Long totalSubmissions, Long solvedProblems, Double averageAccuracy) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.totalSubmissions = totalSubmissions;
        this.solvedProblems = solvedProblems;
        this.averageAccuracy = averageAccuracy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getTotalSubmissions() {
        return totalSubmissions;
    }

    public void setTotalSubmissions(Long totalSubmissions) {
        this.totalSubmissions = totalSubmissions;
    }

    public Long getSolvedProblems() {
        return solvedProblems;
    }

    public void setSolvedProblems(Long solvedProblems) {
        this.solvedProblems = solvedProblems;
    }

    public Double getAverageAccuracy() {
        return averageAccuracy;
    }

    public void setAverageAccuracy(Double averageAccuracy) {
        this.averageAccuracy = averageAccuracy;
    }
}
