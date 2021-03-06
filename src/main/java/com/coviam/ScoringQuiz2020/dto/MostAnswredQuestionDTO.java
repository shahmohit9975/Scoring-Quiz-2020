package com.coviam.ScoringQuiz2020.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.kafka.common.protocol.types.Field;

@Getter
@Setter
@ToString
public class MostAnswredQuestionDTO {
    private String questionId;
    private String questionText;
    private int count;
}
