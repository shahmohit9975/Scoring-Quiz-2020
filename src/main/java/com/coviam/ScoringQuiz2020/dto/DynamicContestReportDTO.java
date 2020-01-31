package com.coviam.ScoringQuiz2020.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class DynamicContestReportDTO {
    private String dynamicContestReportId;
    private String userId;
    private String contestId;
    private int points;
    private int correctAnsCount;
    private int wrongAnsCount;
    private int easyCount;
    private int mediumCount;
    private int difficultCount;
}
