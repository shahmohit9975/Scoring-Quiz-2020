package com.coviam.ScoringQuiz2020.service;

import com.coviam.ScoringQuiz2020.dto.DynamicContestSubmitDTO;
import com.coviam.ScoringQuiz2020.dto.StaticContestSubmitDTO;

public interface ScoreService {
    boolean generatePointsForStaticContest(StaticContestSubmitDTO staticContestSubmitDTO);

    boolean generatePointsForDynamicContest(DynamicContestSubmitDTO dynamicContestSubmitDTO);
}
