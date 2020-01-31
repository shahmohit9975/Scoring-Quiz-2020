package com.coviam.ScoringQuiz2020.service.impl;

import com.coviam.ScoringQuiz2020.document.DynamicContestSubmit;
import com.coviam.ScoringQuiz2020.dto.DynamicContestSubmitDTO;
import com.coviam.ScoringQuiz2020.repository.DynamicContestSubmitRepository;
import com.coviam.ScoringQuiz2020.service.DynamicContestSubmitService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DynamicContestSubmitServiceImpl implements DynamicContestSubmitService {

    @Autowired
    DynamicContestSubmitRepository dynamicContestSubmitRepository;

    @Override
    public boolean addDynamicContestSubmitRecord(DynamicContestSubmitDTO dynamicContestSubmitDTO) {
        DynamicContestSubmit dynamicContestSubmit = new DynamicContestSubmit();
        BeanUtils.copyProperties(dynamicContestSubmitDTO, dynamicContestSubmit);
        dynamicContestSubmitRepository.save(dynamicContestSubmit);
        return true;
    }
}
