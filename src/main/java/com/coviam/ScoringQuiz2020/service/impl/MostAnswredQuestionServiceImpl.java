package com.coviam.ScoringQuiz2020.service.impl;

import com.coviam.ScoringQuiz2020.document.MostAnswredQuestion;
import com.coviam.ScoringQuiz2020.repository.MostAnswredQuestionRepository;
import com.coviam.ScoringQuiz2020.service.MostAnswredQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MostAnswredQuestionServiceImpl implements MostAnswredQuestionService {
    @Autowired
    MostAnswredQuestionRepository mostAnswredQuestionRepository;

    @Override
    public void save(Map<String, Integer> correctAnswerQuestionIds) {
        correctAnswerQuestionIds.forEach((questionId, count) -> {
            Optional<MostAnswredQuestion> byId = mostAnswredQuestionRepository.findById(questionId);
            if (byId.isPresent()) {
                MostAnswredQuestion mostAnswredQuestion = byId.get();
                mostAnswredQuestion.setCount(mostAnswredQuestion.getCount() + count);
                mostAnswredQuestionRepository.save(mostAnswredQuestion);
            } else {
                MostAnswredQuestion mostAnswredQuestion = new MostAnswredQuestion();
                mostAnswredQuestion.setQuestionId(questionId);
                mostAnswredQuestion.setCount(count);
                mostAnswredQuestionRepository.save(mostAnswredQuestion);
            }

        });
    }

    @Override
    public List<MostAnswredQuestion> getAllRecords() {

        Comparator<MostAnswredQuestion> compareById = new Comparator<MostAnswredQuestion>() {
            @Override
            public int compare(MostAnswredQuestion o1, MostAnswredQuestion o2) {
                return String.valueOf(o1.getCount()).compareTo(String.valueOf(o2.getCount()));
            }
        };
        ArrayList<MostAnswredQuestion> all = (ArrayList<MostAnswredQuestion>) mostAnswredQuestionRepository.findAll();
        Collections.sort(all, compareById.reversed());

        return all;
    }
}



