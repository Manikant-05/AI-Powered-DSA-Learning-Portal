package com.dsaportal.dto;

import com.dsaportal.entity.Problem;
import com.dsaportal.entity.TestCase;

import java.time.LocalDateTime;
import java.util.List;

public class ProblemDto {
    private Long id;
    private String title;
    private String description;
    private String difficulty;
    private String topic;
    private String inputFormat;
    private String outputFormat;
    private String constraints;
    private String hint;
    private Integer timeLimit;
    private Integer memoryLimit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TestCaseDto> testCases;

    // Constructors
    public ProblemDto() {}

    public ProblemDto(Problem problem) {
        this.id = problem.getId();
        this.title = problem.getTitle();
        this.description = problem.getDescription();
        this.difficulty = problem.getDifficulty().name();
        this.topic = problem.getTopic().name();
        this.inputFormat = problem.getInputFormat();
        this.outputFormat = problem.getOutputFormat();
        this.constraints = problem.getConstraints();
        this.hint = problem.getHint();
        this.timeLimit = problem.getTimeLimit();
        this.memoryLimit = problem.getMemoryLimit();
        this.createdAt = problem.getCreatedAt();
        this.updatedAt = problem.getUpdatedAt();
        
        if (problem.getTestCases() != null) {
            this.testCases = problem.getTestCases().stream()
                    .map(TestCaseDto::new)
                    .toList();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getInputFormat() {
        return inputFormat;
    }

    public void setInputFormat(String inputFormat) {
        this.inputFormat = inputFormat;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public String getConstraints() {
        return constraints;
    }

    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Integer getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(Integer memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<TestCaseDto> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCaseDto> testCases) {
        this.testCases = testCases;
    }

    public static class TestCaseDto {
        private Long id;
        private String inputData;
        private String expectedOutput;
        private Boolean isSample;

        public TestCaseDto() {}

        public TestCaseDto(TestCase testCase) {
            this.id = testCase.getId();
            this.inputData = testCase.getInputData();
            this.expectedOutput = testCase.getExpectedOutput();
            this.isSample = testCase.getIsSample();
        }

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getInputData() {
            return inputData;
        }

        public void setInputData(String inputData) {
            this.inputData = inputData;
        }

        public String getExpectedOutput() {
            return expectedOutput;
        }

        public void setExpectedOutput(String expectedOutput) {
            this.expectedOutput = expectedOutput;
        }

        public Boolean getIsSample() {
            return isSample;
        }

        public void setIsSample(Boolean isSample) {
            this.isSample = isSample;
        }
    }
}
