package com.coviam.ScoringQuiz2020.service;

public interface ReportStatusService {
    boolean checkOrGenerateStaticContestReport(String contestId);

    boolean addRank(String contestId);
}
