package com.coviam.ScoringQuiz2020.service;

import com.coviam.ScoringQuiz2020.dto.UserRecordsDTO;

import java.util.List;

public interface UserRecordsService {
    void updateRecords(List<UserRecordsDTO> userRecordsDTOS);

    UserRecordsDTO getRecords(String userId);
}
