package com.coviam.ScoringQuiz2020.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class QuestionIdAndTextListDTO {
    private String questionId;
    private String questionText;
}
