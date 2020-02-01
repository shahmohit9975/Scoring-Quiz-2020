package com.coviam.ScoringQuiz2020.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRecordsDTO {
    private String userId;
    private int easy;
    private int medium;
    private int difficult;
    private int correctAnswer;
    private int points;
}
