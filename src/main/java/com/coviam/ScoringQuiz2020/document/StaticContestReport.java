package com.coviam.ScoringQuiz2020.document;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = StaticContestReport.COLLECTION_NAME)
public class StaticContestReport {
    public static final String COLLECTION_NAME = "staticContestReport";
    @Id
    private String staticContestReportId;
    private String userId;
    private String contestId;
    private int noOfSkips;
    private int correctAnsCount;
    private int wrongAnsCount;
    private int points;
    private int easyCount;
    private int mediumCount;
    private int difficultCount;
    private long totalTimeTaken;
    private int rank;
    private String userName;
}
