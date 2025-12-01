package com.dsaportal.service;

import com.dsaportal.entity.Problem;
import com.dsaportal.entity.Submission;
import com.dsaportal.repository.ProblemRepository;
import com.dsaportal.repository.SubmissionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GeminiService {
    
    @Autowired
    private ProblemRepository problemRepository;
    
    @Autowired
    private SubmissionRepository submissionRepository;
    
    @Value("${gemini.api-key}")
    private String geminiApiKey;
    
    @Value("${gemini.base-url}")
    private String geminiBaseUrl;
    
    private final WebClient webClient;
    
    private final ObjectMapper objectMapper;

    public GeminiService() {
        this.webClient = WebClient.builder().build();
        this.objectMapper = new ObjectMapper();
    }
    
    public List<Problem> getRecommendedProblems(Long userId) {
        try {
            // Get user's performance data
            Map<String, Double> accuracyByTopic = getUserAccuracyByTopic(userId);
            
            // Prepare prompt for Gemini
            String prompt = buildRecommendationPrompt(accuracyByTopic);
            
            // Call Gemini API
            String response = callGeminiAPI(prompt);
            
            // Parse response and get recommended problems
            return parseRecommendations(response, userId);
            
        } catch (Exception e) {
            System.err.println("Error getting recommendations from Gemini: " + e.getMessage());
            // Return some default problems if Gemini fails
            return problemRepository.findAll().stream()
                    .limit(5)
                    .collect(Collectors.toList());
        }
    }
    
    private Map<String, Double> getUserAccuracyByTopic(Long userId) {
        List<Object[]> results = submissionRepository.getAccuracyByTopicForUser(userId);
        Map<String, Double> accuracyByTopic = new HashMap<>();
        
        for (Object[] result : results) {
            String topic = (String) result[0];
            Double accuracy = (Double) result[1];
            accuracyByTopic.put(topic, accuracy);
        }
        
        return accuracyByTopic;
    }
    
    private String buildRecommendationPrompt(Map<String, Double> accuracyByTopic) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an AI tutor. A student has the following performance:\n\n");
        
        if (accuracyByTopic.isEmpty()) {
            prompt.append("This is a new student with no submission history.\n");
        } else {
            for (Map.Entry<String, Double> entry : accuracyByTopic.entrySet()) {
                prompt.append("Accuracy: ").append(String.format("%.1f", entry.getValue() * 100))
                      .append("% in ").append(entry.getKey()).append("\n");
            }
        }
        
        prompt.append("\nSuggest 3 problems by topic and difficulty to help them improve. ");
        prompt.append("Format your response as JSON with this structure:\n");
        prompt.append("{\n");
        prompt.append("  \"recommendations\": [\n");
        prompt.append("    {\"topic\": \"TOPIC_NAME\", \"difficulty\": \"EASY|MEDIUM|HARD\"},\n");
        prompt.append("    {\"topic\": \"TOPIC_NAME\", \"difficulty\": \"EASY|MEDIUM|HARD\"},\n");
        prompt.append("    {\"topic\": \"TOPIC_NAME\", \"difficulty\": \"EASY|MEDIUM|HARD\"}\n");
        prompt.append("  ]\n");
        prompt.append("}\n");
        
        return prompt.toString();
    }
    
    private String callGeminiAPI(String prompt) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> content = new HashMap<>();
            Map<String, Object> part = new HashMap<>();
            
            part.put("text", prompt);
            content.put("parts", Arrays.asList(part));
            requestBody.put("contents", Arrays.asList(content));
            
            return webClient.post()
                    .uri(geminiBaseUrl + "/models/gemini-1.5-flash:generateContent?key=" + geminiApiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
                    
        } catch (Exception e) {
            System.err.println("Error calling Gemini API: " + e.getMessage());
            return "{\"recommendations\": [{\"topic\": \"ARRAYS\", \"difficulty\": \"EASY\"}, {\"topic\": \"STRINGS\", \"difficulty\": \"EASY\"}, {\"topic\": \"TREES\", \"difficulty\": \"MEDIUM\"}]}";
        }
    }
    
    private List<Problem> parseRecommendations(String response, Long userId) {
        try {
            // Simple JSON parsing - in production, use a proper JSON library
            List<Problem> recommendations = new ArrayList<>();
            
            // Get solved problem IDs to avoid recommending already solved problems
            Set<Long> solvedProblemIds = submissionRepository.findByUserIdAndStatus(userId, Submission.Status.ACCEPTED)
                    .stream()
                    .map(s -> s.getProblem().getId())
                    .collect(Collectors.toSet());
            
            // Parse the response and find matching problems
            // This is a simplified parser - in production, use Jackson or Gson
            if (response.contains("ARRAYS")) {
                recommendations.addAll(problemRepository.findByTopic(Problem.Topic.ARRAYS)
                        .stream()
                        .filter(p -> !solvedProblemIds.contains(p.getId()))
                        .limit(1)
                        .collect(Collectors.toList()));
            }
            if (response.contains("STRINGS")) {
                recommendations.addAll(problemRepository.findByTopic(Problem.Topic.STRINGS)
                        .stream()
                        .filter(p -> !solvedProblemIds.contains(p.getId()))
                        .limit(1)
                        .collect(Collectors.toList()));
            }
            if (response.contains("TREES")) {
                recommendations.addAll(problemRepository.findByTopic(Problem.Topic.TREES)
                        .stream()
                        .filter(p -> !solvedProblemIds.contains(p.getId()))
                        .limit(1)
                        .collect(Collectors.toList()));
            }
            
            // If no recommendations found, return some default problems
            if (recommendations.isEmpty()) {
                recommendations = problemRepository.findAll()
                        .stream()
                        .filter(p -> !solvedProblemIds.contains(p.getId()))
                        .limit(3)
                        .collect(Collectors.toList());
            }
            
            return recommendations;
            
        } catch (Exception e) {
            System.err.println("Error parsing Gemini response: " + e.getMessage());
            return problemRepository.findAll().stream().limit(3).collect(Collectors.toList());
        }
    }

    public Optional<Problem> suggestNextProblem(Long userId, Problem currentProblem, double score) {
        try {
            String prompt = buildNextProblemPrompt(currentProblem, score);
            String response = callGeminiAPI(prompt);
            Optional<ProblemSelection> selection = parseNextProblemSuggestion(response, currentProblem);
            if (selection.isPresent()) {
                Optional<Problem> aiPick = pickSuggestedProblem(userId, selection.get().topic(), selection.get().difficulty());
                if (aiPick.isPresent()) {
                    return aiPick;
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting next problem suggestion from Gemini: " + e.getMessage());
        }

        Problem.Difficulty fallbackDifficulty = adjustDifficulty(currentProblem.getDifficulty(), score);
        return pickSuggestedProblem(userId, currentProblem.getTopic(), fallbackDifficulty);
    }

    private String buildNextProblemPrompt(Problem currentProblem, double score) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an AI tutor helping a student plan their next problem.\n");
        prompt.append("The student just solved: ").append(currentProblem.getTitle()).append(" (")
                .append(currentProblem.getTopic()).append(", ").append(currentProblem.getDifficulty()).append(").\n");
        prompt.append("Their evaluation score was ").append(String.format("%.1f", score)).append(" out of 100.\n");
        prompt.append("Recommend the topic and difficulty for the next problem in JSON:\n");
        prompt.append("{ \"topic\": \"ARRAYS\", \"difficulty\": \"MEDIUM\" }\n");
        prompt.append("Focus on steady progression with practical tips.\n");
        return prompt.toString();
    }

    private Optional<ProblemSelection> parseNextProblemSuggestion(String response, Problem currentProblem) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode candidates = rootNode.get("candidates");
            if (candidates != null && candidates.isArray() && candidates.size() > 0) {
                JsonNode content = candidates.get(0).get("content");
                if (content != null) {
                    JsonNode parts = content.get("parts");
                    if (parts != null && parts.isArray() && parts.size() > 0) {
                        String text = parts.get(0).get("text").asText();
                        JsonNode suggestionNode = objectMapper.readTree(text);
                        Problem.Topic topic = parseTopic(suggestionNode.path("topic").asText(), currentProblem.getTopic());
                        Problem.Difficulty difficulty = parseDifficulty(suggestionNode.path("difficulty").asText(), currentProblem.getDifficulty());
                        return Optional.of(new ProblemSelection(topic, difficulty));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing next problem suggestion: " + e.getMessage());
        }
        return Optional.empty();
    }

    private Problem.Topic parseTopic(String raw, Problem.Topic fallback) {
        if (raw == null || raw.isBlank()) {
            return fallback;
        }
        try {
            return Problem.Topic.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return fallback;
        }
    }

    private Problem.Difficulty parseDifficulty(String raw, Problem.Difficulty fallback) {
        if (raw == null || raw.isBlank()) {
            return fallback;
        }
        try {
            return Problem.Difficulty.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return fallback;
        }
    }

    private Problem.Difficulty adjustDifficulty(Problem.Difficulty current, double score) {
        if (score >= 80) {
            return switch (current) {
                case EASY -> Problem.Difficulty.MEDIUM;
                case MEDIUM -> Problem.Difficulty.HARD;
                case HARD -> Problem.Difficulty.HARD;
            };
        } else if (score < 50) {
            return switch (current) {
                case HARD -> Problem.Difficulty.MEDIUM;
                case MEDIUM -> Problem.Difficulty.EASY;
                case EASY -> Problem.Difficulty.EASY;
            };
        }
        return current;
    }

    private Optional<Problem> pickSuggestedProblem(Long userId, Problem.Topic topic, Problem.Difficulty difficulty) {
        List<Submission> solvedSubmissions = submissionRepository.findByUserIdAndStatus(userId, Submission.Status.ACCEPTED);
        Set<Long> solvedProblemIds = solvedSubmissions.stream()
                .map(submission -> submission.getProblem().getId())
                .collect(Collectors.toSet());

        List<Problem> candidates = problemRepository.findByDifficultyAndTopic(difficulty, topic).stream()
                .filter(problem -> !solvedProblemIds.contains(problem.getId()))
                .collect(Collectors.toList());

        if (!candidates.isEmpty()) {
            return Optional.of(candidates.get(new Random().nextInt(candidates.size())));
        }

        // fallback to any difficulty within topic
        List<Problem> fallback = problemRepository.findByTopic(topic).stream()
                .filter(problem -> !solvedProblemIds.contains(problem.getId()))
                .collect(Collectors.toList());
        if (!fallback.isEmpty()) {
            return Optional.of(fallback.get(0));
        }

        // final fallback - any unsolved problem
        List<Problem> allFallback = problemRepository.findAll().stream()
                .filter(problem -> !solvedProblemIds.contains(problem.getId()))
                .collect(Collectors.toList());
        if (!allFallback.isEmpty()) {
            return Optional.of(allFallback.get(0));
        }

        return Optional.empty();
    }

    private record ProblemSelection(Problem.Topic topic, Problem.Difficulty difficulty) {}
}
