package com.coviam.ScoringQuiz2020.document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = UserRecords.COLLECTION_NAME)
public class UserRecords {
    public static final String COLLECTION_NAME = "userRecords";
    @Id
    private String userId;
    private int easy;
    private int medium;
    private int difficult;
    private int correctAnswer;
    private int points;
}
