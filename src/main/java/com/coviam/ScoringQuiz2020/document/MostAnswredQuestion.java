package com.coviam.ScoringQuiz2020.document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = MostAnswredQuestion.COLLECTION_NAME)
public class MostAnswredQuestion {
    public static final String COLLECTION_NAME = "mostAnswredQuestion";
    @Id
    private String questionId;
    private Integer count;
}
