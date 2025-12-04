package com.dsaportal.service;

import com.dsaportal.entity.Submission;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class Judge0Service {
    
    public static class Judge0Result {
        private Submission.Status status;
        private String errorMessage;
        private String output;
        private Integer timeTaken;
        private Integer memoryUsed;

        public Judge0Result(Submission.Status status) {
            this.status = status;
        }

        public Submission.Status getStatus() { return status; }
        public void setStatus(Submission.Status status) { this.status = status; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public String getOutput() { return output; }
        public void setOutput(String output) { this.output = output; }
        public Integer getTimeTaken() { return timeTaken; }
        public void setTimeTaken(Integer timeTaken) { this.timeTaken = timeTaken; }
        public Integer getMemoryUsed() { return memoryUsed; }
        public void setMemoryUsed(Integer memoryUsed) { this.memoryUsed = memoryUsed; }
    }
    
    @Value("${judge0.base-url}")
    private String judge0BaseUrl;
    
    @Value("${judge0.api-key}")
    private String judge0ApiKey;
    
    @Value("${judge0.host}")
    private String judge0Host;
    
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    
    public Judge0Service() {
        this.webClient = WebClient.builder().build();
        this.objectMapper = new ObjectMapper();
    }
    
    public String submitCode(Submission submission) {
        try {
            System.out.println("Submitting code to Judge0...");
            System.out.println("Judge0 URL: " + judge0BaseUrl);
            System.out.println("Language: " + submission.getLanguage());
            System.out.println("Code: " + submission.getCode().substring(0, Math.min(100, submission.getCode().length())));
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("source_code", submission.getCode());
            requestBody.put("language_id", getLanguageId(submission.getLanguage()));
            
            String testInput = getTestInput(submission);
            String expectedOutput = getExpectedOutput(submission);
            
            // Only add stdin and expected_output if we have test cases
            if (!testInput.isEmpty()) {
                requestBody.put("stdin", testInput);
            }
            if (!expectedOutput.isEmpty()) {
                requestBody.put("expected_output", expectedOutput);
            }
            
            requestBody.put("cpu_time_limit", 2.0); // 2 seconds default
            requestBody.put("memory_limit", 128000); // 128MB default
            
            System.out.println("Request body: " + requestBody);
            
            String response = webClient.post()
                    .uri(judge0BaseUrl + "/submissions")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header("X-RapidAPI-Key", judge0ApiKey)
                    .header("X-RapidAPI-Host", judge0Host)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            System.out.println("Judge0 response: " + response);
            
            // Parse response to get token
            return extractTokenFromResponse(response);
            
        } catch (Exception e) {
            System.err.println("Error submitting code to Judge0: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public Judge0Result getSubmissionResult(String token) {
        try {
            System.out.println("Getting submission result for token: " + token);
            
            String response = webClient.get()
                    .uri(judge0BaseUrl + "/submissions/" + token + "?base64_encoded=true&fields=stdout,stderr,status_id,time,memory,compile_output,message")
                    .header("X-RapidAPI-Key", judge0ApiKey)
                    .header("X-RapidAPI-Host", judge0Host)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            System.out.println("Judge0 result response: " + response);
            
            return parseSubmissionResult(response);
            
        } catch (Exception e) {
            System.err.println("Error getting submission result from Judge0: " + e.getMessage());
            e.printStackTrace();
            Judge0Result result = new Judge0Result(Submission.Status.RUNTIME_ERROR);
            result.setErrorMessage(e.getMessage());
            return result;
        }
    }
    
    public Judge0Result getSubmissionResultWithPolling(String token) {
        int maxAttempts = 10;
        int attempt = 0;
        
        while (attempt < maxAttempts) {
            try {
                Judge0Result result = getSubmissionResult(token);
                
                // If status is not pending, return it
                if (result.getStatus() != Submission.Status.PENDING) {
                    return result;
                }
                
                // Wait 2 seconds before next attempt
                Thread.sleep(2000);
                attempt++;
                
            } catch (Exception e) {
                System.err.println("Error in polling attempt " + attempt + ": " + e.getMessage());
                attempt++;
            }
        }
        
        // If max attempts reached, return runtime error
        return new Judge0Result(Submission.Status.RUNTIME_ERROR);
    }
    
    private int getLanguageId(Submission.Language language) {
        switch (language) {
            case PYTHON: return 71; // Python 3
            case JAVA: return 62; // Java
            case CPP: return 54; // C++ (GCC 9.2.0)
            case JAVASCRIPT: return 63; // Node.js 12.14.0
            case C: return 50; // C (GCC 9.2.0)
            default: return 71; // Default to Python
        }
    }
    
    private String getTestInput(Submission submission) {
        // Get the first test case input
        if (submission.getProblem().getTestCases() != null && !submission.getProblem().getTestCases().isEmpty()) {
            String input = submission.getProblem().getTestCases().get(0).getInputData();
            System.out.println("Test input: " + input);
            return input != null ? input : "";
        }
        System.out.println("No test cases found, using empty input");
        return "";
    }
    
    private String getExpectedOutput(Submission submission) {
        // Get the first test case expected output
        if (submission.getProblem().getTestCases() != null && !submission.getProblem().getTestCases().isEmpty()) {
            String output = submission.getProblem().getTestCases().get(0).getExpectedOutput();
            System.out.println("Expected output: " + output);
            return output != null ? output : "";
        }
        System.out.println("No test cases found, using empty expected output");
        return "";
    }
    
    private String extractTokenFromResponse(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            if (jsonNode.has("token")) {
                return jsonNode.get("token").asText();
            }
        } catch (Exception e) {
            System.err.println("Error parsing token from response: " + e.getMessage());
        }
        return null;
    }
    
    private Judge0Result parseSubmissionResult(String response) {
        try {
            System.out.println("Parsing status from response: " + response);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            Submission.Status status = Submission.Status.RUNTIME_ERROR;
            String errorMessage = null;
            String output = null;
            Integer timeTaken = null;
            Integer memoryUsed = null;

            if (jsonNode.has("status_id")) {
                int statusId = jsonNode.get("status_id").asInt();
                System.out.println("Judge0 status ID: " + statusId);
                
                switch (statusId) {
                    case 1: // In Queue
                    case 2: // Processing
                        status = Submission.Status.PENDING;
                        break;
                    case 3: // Accepted
                        status = Submission.Status.ACCEPTED;
                        break;
                    case 4: // Wrong Answer
                        status = Submission.Status.WRONG_ANSWER;
                        break;
                    case 5: // Time Limit Exceeded
                        status = Submission.Status.TIME_LIMIT_EXCEEDED;
                        break;
                    case 6: // Memory Limit Exceeded
                        status = Submission.Status.MEMORY_LIMIT_EXCEEDED;
                        break;
                    case 7: // Output Limit Exceeded
                    case 8: // Presentation Error
                    case 9: // Runtime Error
                    case 10: // Compilation Error
                    case 11: // Runtime Error (SIGSEGV)
                    case 12: // Runtime Error (SIGFPE)
                    case 13: // Runtime Error (SIGABRT)
                    case 14: // Runtime Error (NZEC)
                    case 15: // Runtime Error (Other)
                        status = statusId == 10 ? Submission.Status.COMPILATION_ERROR : Submission.Status.RUNTIME_ERROR;
                        break;
                    default: 
                        System.out.println("Unknown status ID: " + statusId);
                        status = Submission.Status.RUNTIME_ERROR;
                }
            }
            
            // Decode Base64 fields
            if (jsonNode.has("stdout") && !jsonNode.get("stdout").isNull()) {
                output = new String(Base64.getDecoder().decode(jsonNode.get("stdout").asText()), StandardCharsets.UTF_8);
            }
            
            if (jsonNode.has("stderr") && !jsonNode.get("stderr").isNull()) {
                String stderr = new String(Base64.getDecoder().decode(jsonNode.get("stderr").asText()), StandardCharsets.UTF_8);
                errorMessage = (errorMessage == null ? "" : errorMessage + "\n") + stderr;
            }
            
            if (jsonNode.has("compile_output") && !jsonNode.get("compile_output").isNull()) {
                String compileOutput = new String(Base64.getDecoder().decode(jsonNode.get("compile_output").asText()), StandardCharsets.UTF_8);
                errorMessage = (errorMessage == null ? "" : errorMessage + "\n") + compileOutput;
            }
            
            if (jsonNode.has("message") && !jsonNode.get("message").isNull()) {
                String message = new String(Base64.getDecoder().decode(jsonNode.get("message").asText()), StandardCharsets.UTF_8);
                errorMessage = (errorMessage == null ? "" : errorMessage + "\n") + message;
            }

            if (jsonNode.has("time") && !jsonNode.get("time").isNull()) {
                timeTaken = (int) (jsonNode.get("time").asDouble() * 1000);
            }

            if (jsonNode.has("memory") && !jsonNode.get("memory").isNull()) {
                memoryUsed = jsonNode.get("memory").asInt();
            }

            Judge0Result result = new Judge0Result(status);
            result.setErrorMessage(errorMessage);
            result.setOutput(output);
            result.setTimeTaken(timeTaken);
            result.setMemoryUsed(memoryUsed);
            
            return result;
            
        } catch (Exception e) {
            System.err.println("Error parsing submission status: " + e.getMessage());
            e.printStackTrace();
            Judge0Result result = new Judge0Result(Submission.Status.RUNTIME_ERROR);
            result.setErrorMessage("Error parsing response: " + e.getMessage());
            return result;
        }
    }
}
