package com.coviam.ScoringQuiz2020.repository;

import com.coviam.ScoringQuiz2020.document.DynamicContestReport;
import com.coviam.ScoringQuiz2020.dto.DynamicContestReportDTO;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DynamicContestReportRepository extends MongoRepository<DynamicContestReport, String> {
    List<DynamicContestReportDTO> findByContestIdAndUserId(String contestId, String userId);

    List<DynamicContestReportDTO> findByContestIdOrderByPointsDesc(String contestId);
}
