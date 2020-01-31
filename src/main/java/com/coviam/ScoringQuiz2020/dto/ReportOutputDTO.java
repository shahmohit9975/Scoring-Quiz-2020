package com.coviam.ScoringQuiz2020.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReportOutputDTO {
    private String userID;
    private int points;
    private long timetaken;
    private int rank;
}
