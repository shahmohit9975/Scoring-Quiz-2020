package com.coviam.ScoringQuiz2020.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@ToString
public class DynamicContestSubmitDTO {
    private String userId;
    private String contestId;
    private String questionId;
    private String submittedAns;
    private Date dateAndTime;
    private String userName;
}
