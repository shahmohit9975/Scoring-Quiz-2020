package com.coviam.ScoringQuiz2020.repository;

import com.coviam.ScoringQuiz2020.document.StaticContestReport;
import com.coviam.ScoringQuiz2020.dto.StaticContestReportDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaticContestReportRepository extends MongoRepository<StaticContestReport, String> {
    List<StaticContestReportDTO> findByContestIdAndUserId(String contestId, String userID);

    List<StaticContestReport> findByContestIdOrderByPointsDescTotalTimeTakenAscCorrectAnsCountDescNoOfSkipsAsc(String contestId);

    List<StaticContestReportDTO> findTop10ByContestIdOrderByRankAsc(String contestId);

    List<StaticContestReportDTO> findByContestIdOrderByPointsDesc(String contestId);
}
