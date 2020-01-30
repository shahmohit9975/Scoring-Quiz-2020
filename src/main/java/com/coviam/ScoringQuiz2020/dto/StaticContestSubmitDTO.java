package com.coviam.ScoringQuiz2020.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class StaticContestSubmitDTO {
    private String userID;
    private String contestId;
    private List<QuestionAnsDTO> questionAnsDTOList;
    private int noOfskips;
    private int starting;
}
