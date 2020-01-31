package com.coviam.ScoringQuiz2020.document;


import com.coviam.ScoringQuiz2020.dto.StaticContesQuestionsAndAnswerDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = StaticContestSubmit.COLLECTION_NAME)
public class StaticContestSubmit {
    public static final String COLLECTION_NAME = "staticContestSubmit";
    @Id
    private String staticContestSubmitId;
    private String userId;
    private String contestId;
    private int noOfSkips;
    private List<StaticContesQuestionsAndAnswerDTO> staticContesQuestionsAndAnswersDTO;
    private Date dateAndTime;
    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = new Date();
    }
}
