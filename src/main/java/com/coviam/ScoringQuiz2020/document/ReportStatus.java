package com.coviam.ScoringQuiz2020.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = ReportStatus.COLLECTION_NAME)
public class ReportStatus {
    public static final String COLLECTION_NAME = "reportStatus";
    @Id
    private String reportStatusId;
    private String contestId;
    private boolean contestType;
}
