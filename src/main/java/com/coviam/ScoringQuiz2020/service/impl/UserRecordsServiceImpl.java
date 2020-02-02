package com.coviam.ScoringQuiz2020.service.impl;

import com.coviam.ScoringQuiz2020.document.UserRecords;
import com.coviam.ScoringQuiz2020.dto.UserRecordsDTO;
import com.coviam.ScoringQuiz2020.repository.UserRecordsRepository;
import com.coviam.ScoringQuiz2020.service.UserRecordsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRecordsServiceImpl implements UserRecordsService {
    @Autowired
    UserRecordsRepository userRecordsRepository;

    @Override
    public void updateRecords(List<UserRecordsDTO> userRecordsDTOS) {
        for (UserRecordsDTO obj : userRecordsDTOS) {
            boolean check = userRecordsRepository.existsById(obj.getUserId());
            if (check) {
                UserRecords byId = userRecordsRepository.findById(obj.getUserId()).get();
                byId.setCorrectAnswer(byId.getCorrectAnswer() + obj.getCorrectAnswer());
                byId.setPoints(byId.getPoints() + obj.getPoints());
                byId.setEasy(byId.getEasy() + obj.getEasy());
                byId.setMedium(byId.getMedium() + obj.getMedium());
                byId.setDifficult(byId.getDifficult() + obj.getDifficult());
                UserRecords save = userRecordsRepository.save(byId);
            } else {
                UserRecords userRecords = new UserRecords();
                BeanUtils.copyProperties(obj, userRecords);
                UserRecords save = userRecordsRepository.save(userRecords);
            }
        }
    }

    @Override
    public UserRecordsDTO getRecords(String userId) {
        final Optional<UserRecords> byId = userRecordsRepository.findById(userId);
        UserRecordsDTO userRecordsDTO = new UserRecordsDTO();
        if (byId.isPresent()) {
            BeanUtils.copyProperties(byId.get(), userRecordsDTO);
        }
        return userRecordsDTO;
    }
}
/*
check ==>UserRecords(userId=u2, easy=6, medium=6, difficult=0, correctAnswer=12, points=235)
check ==>UserRecords(userId=u1, easy=6, medium=5, difficult=0, correctAnswer=11, points=115)
 */