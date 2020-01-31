package com.coviam.ScoringQuiz2020.repository;

import com.coviam.ScoringQuiz2020.document.DynamicContestSubmit;
import com.coviam.ScoringQuiz2020.dto.DynamicContestSubmitDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DynamicContestSubmitRepository extends MongoRepository<DynamicContestSubmit, String> {
    List<DynamicContestSubmitDTO> findByContestId(String contestId);

    List<DynamicContestSubmitDTO> findByContestIdAndQuestionIdAndSubmittedAnsOrderByDateAndTimeAsc(String contestId, String questionId, String answers);
}
