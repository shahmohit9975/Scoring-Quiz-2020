package com.coviam.ScoringQuiz2020.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class DynamicContestSubmitDTO {
    private String userID;
    private String contestId;
    private String questionId;
    private String answer;
    private int timetaken;
}
