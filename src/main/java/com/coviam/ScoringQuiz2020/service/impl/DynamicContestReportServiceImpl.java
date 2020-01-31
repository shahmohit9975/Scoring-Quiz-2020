package com.coviam.ScoringQuiz2020.service.impl;

import com.coviam.ScoringQuiz2020.document.DynamicContestReport;
import com.coviam.ScoringQuiz2020.dto.DynamicContestReportDTO;
import com.coviam.ScoringQuiz2020.dto.StaticContestReportDTO;
import com.coviam.ScoringQuiz2020.repository.DynamicContestReportRepository;
import com.coviam.ScoringQuiz2020.service.DynamicContestReportService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DynamicContestReportServiceImpl implements DynamicContestReportService {
    @Autowired
    DynamicContestReportRepository dynamicContestReportRepository;

    @Override
    public List<DynamicContestReportDTO> getReportForUser(String contestId, String userId) {
        List<DynamicContestReportDTO> list = new ArrayList<DynamicContestReportDTO>();
        List<DynamicContestReportDTO> dynamicContestReportDTOS = dynamicContestReportRepository.findByContestIdAndUserId(contestId, userId);

        if (dynamicContestReportDTOS == null || dynamicContestReportDTOS.size() == 0) {
            DynamicContestReportDTO dynamicContestReportDTO = new DynamicContestReportDTO();
            dynamicContestReportDTO.setAttend(false);
            list.add(dynamicContestReportDTO);
        } else {
            DynamicContestReportDTO dynamicContestReportDTO = new DynamicContestReportDTO();
            BeanUtils.copyProperties(dynamicContestReportDTOS.get(0), dynamicContestReportDTO);
            list.add(dynamicContestReportDTO);
        }
        List<DynamicContestReportDTO> results = dynamicContestReportRepository.findTop10ByContestIdOrderByRankAsc(contestId);
        list.addAll(results);
        return list;
    }

    @Override
    public List<DynamicContestReportDTO> getReportForMaster(String contestId) {
        return dynamicContestReportRepository.findByContestId(contestId);
    }
}
