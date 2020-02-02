package com.coviam.ScoringQuiz2020.repository;

import com.coviam.ScoringQuiz2020.document.MostAnswredQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MostAnswredQuestionRepository extends MongoRepository<MostAnswredQuestion, String> {
}
