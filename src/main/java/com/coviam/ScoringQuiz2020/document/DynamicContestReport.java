package com.coviam.ScoringQuiz2020.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = DynamicContestReport.COLLECTION_NAME)
public class DynamicContestReport {
    public static final String COLLECTION_NAME = "dynamicContestReport";
    @Id
    private String dynamicContestReportId;
    private String userId;
    private String contestId;
    private int points;
    private int correctAnsCount;
    private int wrongAnsCount;
    private int easyCount;
    private int mediumCount;
    private int difficultCount;
    private int rank;
}
