package com.coviam.ScoringQuiz2020.service;

import com.coviam.ScoringQuiz2020.dto.DynamicContestSubmitDTO;
import com.coviam.ScoringQuiz2020.dto.ReportOutputDTO;
import com.coviam.ScoringQuiz2020.dto.StaticContestSubmitDTO;

import java.util.List;

public interface ScoreService {
    boolean generatePointsForStaticContest(StaticContestSubmitDTO staticContestSubmitDTO);

    boolean generatePointsForDynamicContest(DynamicContestSubmitDTO dynamicContestSubmitDTO);

    List<ReportOutputDTO> getReport(String contestId, String userID,boolean contestType);

}
