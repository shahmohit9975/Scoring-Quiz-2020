package com.coviam.ScoringQuiz2020.service.impl;

import com.coviam.ScoringQuiz2020.document.StaticContestSubmit;
import com.coviam.ScoringQuiz2020.dto.StaticContestSubmitDTO;
import com.coviam.ScoringQuiz2020.repository.StaticContestSubmitRepository;
import com.coviam.ScoringQuiz2020.service.StaticContestSubmitService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaticContestSubmitServiceImpl implements StaticContestSubmitService {
    @Autowired
    StaticContestSubmitRepository staticContestSubmitRepository;

    @Override
    public boolean addStaticContestSubmitRecord(StaticContestSubmitDTO staticContestSubmitDTO) {

        String userId = staticContestSubmitDTO.getUserID();
        String contestId = staticContestSubmitDTO.getContestId();
        List<StaticContestSubmit> staticContestSubmitList = staticContestSubmitRepository.findByUserIdAndContestId(userId, contestId);
        if (staticContestSubmitList == null || staticContestSubmitList.size() == 0) {
            StaticContestSubmit staticContestSubmit = new StaticContestSubmit();
            BeanUtils.copyProperties(staticContestSubmitDTO, staticContestSubmit);
            staticContestSubmitRepository.save(staticContestSubmit);
            return true;
        }
        StaticContestSubmit staticContestSubmit = staticContestSubmitList.get(0);
        staticContestSubmit.setStaticContesQuestionsAndAnswersDTO(staticContestSubmitDTO.getStaticContesQuestionsAndAnswersDTO());
        staticContestSubmit.setNoOfSkips(staticContestSubmit.getNoOfSkips() + staticContestSubmitDTO.getNoOfskips());
        staticContestSubmitRepository.save(staticContestSubmit);
        return true;
    }
}
