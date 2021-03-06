package com.coviam.ScoringQuiz2020.service;

import com.coviam.ScoringQuiz2020.document.MostAnswredQuestion;
import com.coviam.ScoringQuiz2020.dto.MostAnswredQuestionDTO;

import java.util.List;
import java.util.Map;

public interface MostAnswredQuestionService {
    void save(Map<String, Integer> correctAnswerQuestionIds);

    List<MostAnswredQuestionDTO> getAllRecords();
}
