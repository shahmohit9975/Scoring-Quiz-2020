package com.coviam.ScoringQuiz2020.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = DynamicContestReport.COLLECTION_NAME)
public class DynamicContestReport {
    public static final String COLLECTION_NAME = "dynamicContestReport";
    @Id
    private String dynamicContestReportId;
    private String userId;
    private String contestId;
    private String correctAnsCount;
    private String wrongAnsCount;
    private int points;
    private int easyCount;
    private int mediumCount;
    private int difficultCount;
}