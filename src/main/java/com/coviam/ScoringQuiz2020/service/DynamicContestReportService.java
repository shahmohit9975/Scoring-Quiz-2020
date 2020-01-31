package com.coviam.ScoringQuiz2020.service;

import com.coviam.ScoringQuiz2020.dto.DynamicContestReportDTO;

import java.util.List;

public interface DynamicContestReportService {
    List<DynamicContestReportDTO> getReportForUser(String contestId, String userId);

    List<DynamicContestReportDTO> getReportForMaster(String contestId);
}
