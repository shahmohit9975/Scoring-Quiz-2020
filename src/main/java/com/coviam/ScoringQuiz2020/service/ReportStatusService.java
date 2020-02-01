package com.coviam.ScoringQuiz2020.service;

public interface ReportStatusService {

    boolean checkOrGenerateStaticContestReport(String contestId);

    boolean addRankForStaticContest(String contestId);

    boolean checkOrGenerateDynamicContestReport(String contestId);

    boolean addRankForDynamicContest(String contestId);
}
