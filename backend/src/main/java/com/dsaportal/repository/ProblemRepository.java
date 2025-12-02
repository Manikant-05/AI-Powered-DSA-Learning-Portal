package com.dsaportal.repository;

import com.dsaportal.entity.Problem;
import com.dsaportal.entity.Problem.Difficulty;
import com.dsaportal.entity.Problem.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    List<Problem> findByDifficulty(Difficulty difficulty);
    List<Problem> findByTopic(Topic topic);
    List<Problem> findByDifficultyAndTopic(Difficulty difficulty, Topic topic);
    boolean existsByTitle(String title);
    
    @Query("SELECT p FROM Problem p WHERE " +
           "(:difficulty IS NULL OR p.difficulty = :difficulty) AND " +
           "(:topic IS NULL OR p.topic = :topic) AND " +
           "(:search IS NULL OR LOWER(p.title) LIKE :search OR " +
           "LOWER(p.description) LIKE :search)")
    List<Problem> findByFilters(@Param("difficulty") Difficulty difficulty, 
                               @Param("topic") Topic topic, 
                               @Param("search") String search);
    
    @Query("SELECT p FROM Problem p WHERE p.id IN " +
           "(SELECT DISTINCT s.problem.id FROM Submission s WHERE s.user.id = :userId AND s.status = 'ACCEPTED')")
    List<Problem> findSolvedByUser(@Param("userId") Long userId);
    
    @Query("SELECT p FROM Problem p WHERE p.id NOT IN " +
           "(SELECT DISTINCT s.problem.id FROM Submission s WHERE s.user.id = :userId AND s.status = 'ACCEPTED')")
    List<Problem> findUnsolvedByUser(@Param("userId") Long userId);
    
    List<Problem> findTop3ByTopicAndDifficultyAndIdNot(Topic topic, Difficulty difficulty, Long id);
}
