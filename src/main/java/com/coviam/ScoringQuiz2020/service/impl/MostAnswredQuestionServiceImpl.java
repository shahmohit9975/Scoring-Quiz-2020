package com.coviam.ScoringQuiz2020.service.impl;

import com.coviam.ScoringQuiz2020.document.MostAnswredQuestion;
import com.coviam.ScoringQuiz2020.dto.MostAnswredQuestionDTO;
import com.coviam.ScoringQuiz2020.dto.MostAnswredQuestionsId;
import com.coviam.ScoringQuiz2020.dto.QuestionIdAndTextDTO;
import com.coviam.ScoringQuiz2020.dto.QuestionIdAndTextListDTO;
import com.coviam.ScoringQuiz2020.repository.MostAnswredQuestionRepository;
import com.coviam.ScoringQuiz2020.service.MostAnswredQuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class MostAnswredQuestionServiceImpl implements MostAnswredQuestionService {
    @Value("${spring.contest.questionIdAndTextDTO.url}")
    private String questionIdAndTextDTOUri;
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
    public List<MostAnswredQuestionDTO> getAllRecords() {

//        Comparator<MostAnswredQuestion> compareById = new Comparator<MostAnswredQuestion>() {
//            @Override
//            public int compare(MostAnswredQuestion o1, MostAnswredQuestion o2) {
//                return String.valueOf(o1.getCount()).compareTo(String.valueOf(o2.getCount()));
//            }
//        };
//        Collections.sort(all, compareById.reversed());

        List<MostAnswredQuestionDTO> mostAnswredQuestionDTOList = new ArrayList<MostAnswredQuestionDTO>();
        ArrayList<MostAnswredQuestion> all = (ArrayList<MostAnswredQuestion>) mostAnswredQuestionRepository.findTop3ByOrderByCountDesc();
        List<MostAnswredQuestionsId> mostAnswredQuestionsIdList = new ArrayList<MostAnswredQuestionsId>();
        for (MostAnswredQuestion obj : all) {
            MostAnswredQuestionsId mostAnswredQuestionsId = new MostAnswredQuestionsId();
            mostAnswredQuestionsId.setQuestionId(obj.getQuestionId());
            mostAnswredQuestionsIdList.add(mostAnswredQuestionsId);

            MostAnswredQuestionDTO mostAnswredQuestionDTO = new MostAnswredQuestionDTO();
            BeanUtils.copyProperties(obj, mostAnswredQuestionDTO);
            mostAnswredQuestionDTOList.add(mostAnswredQuestionDTO);
        }
        RestTemplate restTemplate = new RestTemplate();
        QuestionIdAndTextDTO responseMessage = restTemplate.postForObject(
                questionIdAndTextDTOUri, mostAnswredQuestionsIdList, QuestionIdAndTextDTO.class
        );
        int index = 0;
        for (QuestionIdAndTextListDTO obj : responseMessage.getQuestionIdAndTextListDTOS()) {
            mostAnswredQuestionDTOList.get(index++).setQuestionText(obj.getQuestionText());
        }

        return mostAnswredQuestionDTOList;
    }
}



