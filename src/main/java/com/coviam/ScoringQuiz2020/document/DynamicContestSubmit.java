package com.coviam.ScoringQuiz2020.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = DynamicContestSubmit.COLLECTION_NAME)
public class DynamicContestSubmit {

    public static final String COLLECTION_NAME = "dynamicContestSubmit";

    @Id
    private String dynamicContestSubmitId;
    private String userId;
    private String contestId;
    private String questionId;
    private String submittedAns;
    private Date dateAndTime;
    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = new Date();
    }
}
