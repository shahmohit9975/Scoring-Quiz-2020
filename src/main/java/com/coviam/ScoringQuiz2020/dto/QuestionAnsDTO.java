package com.coviam.ScoringQuiz2020.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class QuestionAnsDTO {
    private String questionId;
    private String submitteAnswer;
    private long timetaken;
    private Date dateAndTime;
}
