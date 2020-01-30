package com.coviam.ScoringQuiz2020.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QuestionDTO {
    private String questionId;
    private String answers;
    private String difficultyLevel;
}
