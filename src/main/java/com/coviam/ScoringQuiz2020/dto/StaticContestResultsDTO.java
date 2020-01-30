package com.coviam.ScoringQuiz2020.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class StaticContestResultsDTO {

    private int noOfSkipsAllowed;
    private List<QuestionsDTO> questionsDTOList;
}
