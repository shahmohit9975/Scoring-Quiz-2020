package com.coviam.ScoringQuiz2020.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

@Setter
@Getter
@ToString
public class StaticContestReportDTO {
    private String staticContestReportId;
    private String userId;
    private String contestId;
    private int correctAnsCount;
    private int wrongAnsCount;
    private int points;
    private int noOfSkips;
    private int easyCount;
    private int mediumCount;
    private int difficultCount;
    private int totalTimeTaken;
    private boolean attend;
}
