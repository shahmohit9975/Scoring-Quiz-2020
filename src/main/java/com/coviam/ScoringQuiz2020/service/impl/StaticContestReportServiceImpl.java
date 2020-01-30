package com.coviam.ScoringQuiz2020.service.impl;

import com.coviam.ScoringQuiz2020.dto.StaticContestReportDTO;
import com.coviam.ScoringQuiz2020.repository.StaticContestReportRepository;
import com.coviam.ScoringQuiz2020.service.StaticContestReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StaticContestReportServiceImpl implements StaticContestReportService {

    @Autowired
    StaticContestReportRepository staticContestReportRepository;

    @Override
    public List<StaticContestReportDTO> getReport(String contestId, String userID) {
        List<StaticContestReportDTO> list = new ArrayList<StaticContestReportDTO>();
        List<StaticContestReportDTO> staticContestReportDTOS = staticContestReportRepository.findByContestIdAndUserId(contestId, userID);

        if (staticContestReportDTOS == null || staticContestReportDTOS.size() == 0) {
            StaticContestReportDTO staticContestReportDTO = new StaticContestReportDTO();
            staticContestReportDTO.setAttend(false);
            list.add(staticContestReportDTO);
        } else {
            StaticContestReportDTO staticContestReportDTO = new StaticContestReportDTO();
            staticContestReportDTO.setAttend(true);
            list.add(staticContestReportDTO);
        }

        List<StaticContestReportDTO> results = staticContestReportRepository.findTop10ByContestIdOrderByPointsDescTotalTimeTakenAscCorrectAnsCountDescNoOfSkipsAsc(contestId);
        list.addAll(results);
        return list;
    }
}
