package com.coviam.ScoringQuiz2020.dto;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Service
public class QUestionsDTO {
    List<QuestionDTO> questionDTOList;
}
