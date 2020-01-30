package com.coviam.ScoringQuiz2020.service;

import com.coviam.ScoringQuiz2020.dto.StaticContestReportDTO;

import java.util.List;

public interface StaticContestReportService {
    List<StaticContestReportDTO> getReport(String contestId, String userID);
}
