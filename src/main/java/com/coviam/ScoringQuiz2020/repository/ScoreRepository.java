package com.coviam.ScoringQuiz2020.repository;

import com.coviam.ScoringQuiz2020.document.Scoring;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends MongoRepository<Scoring, String> {
    List<Scoring> findByUserIDAndContestIdAndContestType(String userID, String contestId, boolean b);
}
