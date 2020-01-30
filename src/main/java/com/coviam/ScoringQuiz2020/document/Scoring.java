package com.coviam.ScoringQuiz2020.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = Scoring.COLLECTION_NAME)
public class Scoring {
    public static final String COLLECTION_NAME = "scoring";

    @Id
    private String scoringId;
    private String userID;
    private String contestId;
    private int points;
    private boolean contestType;
    private int noOfSkips;
    private int correctAnswer;
    private int wrongAnswer;
}