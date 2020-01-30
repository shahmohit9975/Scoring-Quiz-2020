package com.coviam.ScoringQuiz2020.repository;

import com.coviam.ScoringQuiz2020.document.ReportStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportStatusRepository extends MongoRepository<ReportStatus, String> {
    List<ReportStatus> findByContestIdAndContestType(String contestId, boolean b);
}
