package com.coviam.ScoringQuiz2020.repository;

import com.coviam.ScoringQuiz2020.document.StaticContestSubmit;
import com.coviam.ScoringQuiz2020.dto.StaticContestSubmitDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaticContestSubmitRepository extends MongoRepository<StaticContestSubmit, String> {
    List<StaticContestSubmit> findByUserIdAndContestId(String userId, String contestId);

    List<StaticContestSubmitDTO> findByContestId(String contestId);
}
