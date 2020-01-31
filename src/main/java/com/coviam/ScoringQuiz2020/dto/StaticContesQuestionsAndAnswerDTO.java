package com.coviam.ScoringQuiz2020.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.util.Date;

@Getter
@Setter
@ToString
public class StaticContesQuestionsAndAnswerDTO {
    private String questionId;
    private String submittedAns;
    private long timetaken;
    private Date dateAndTime;
}
