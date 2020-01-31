package com.coviam.ScoringQuiz2020.service.impl;

import com.coviam.ScoringQuiz2020.document.ReportStatus;
import com.coviam.ScoringQuiz2020.document.StaticContestReport;
import com.coviam.ScoringQuiz2020.dto.QuestionDTO;
import com.coviam.ScoringQuiz2020.dto.StaticContesQuestionsAndAnswerDTO;
import com.coviam.ScoringQuiz2020.dto.StaticContestReportDTO;
import com.coviam.ScoringQuiz2020.dto.StaticContestSubmitDTO;
import com.coviam.ScoringQuiz2020.repository.ReportStatusRepository;
import com.coviam.ScoringQuiz2020.repository.StaticContestReportRepository;
import com.coviam.ScoringQuiz2020.repository.StaticContestSubmitRepository;
import com.coviam.ScoringQuiz2020.service.ReportStatusService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportStatusServiceImpl implements ReportStatusService {
    @Autowired
    ReportStatusRepository reportStatusRepository;

    @Autowired
    StaticContestSubmitRepository staticContestSubmitRepository;

    @Autowired
    StaticContestReportRepository staticContestReportRepository;

    @Override
    public boolean checkOrGenerateStaticContestReport(String contestId) {

        List<ReportStatus> reportStatusList = reportStatusRepository.findByContestIdAndContestType(contestId, true);
        if (reportStatusList != null && reportStatusList.size() > 0) {
            return true;
        } else {
            List<StaticContestSubmitDTO> staticContestSubmitDTOS = staticContestSubmitRepository.findByContestId(contestId);
            //TODO : get List<QuestionDTO> questions by contestId
            List<QuestionDTO> questionDTOS = new ArrayList<QuestionDTO>();
            Map<String, QuestionDTO> map = new HashMap<String, QuestionDTO>();
            for (QuestionDTO questionDTO : questionDTOS) {
                map.put(questionDTO.getQuestionId(), questionDTO);
            }
            for (StaticContestSubmitDTO obj : staticContestSubmitDTOS) {
                StaticContestReport staticContestReport = new StaticContestReport();
                BeanUtils.copyProperties(obj, staticContestReport);
                int correctAnsCount = 0;
                int wrongAnsCount = 0;
                int points = 0;
                int easyCount = 0;
                int mediumCount = 0;
                int difficultCount = 0;
                long totalTimeTaken = 0;

                if (obj.getNoOfskips() == 0) {
                    points += 5;
                }
                for (StaticContesQuestionsAndAnswerDTO staticContesQuestionsAndAnswerDTO : obj.getStaticContesQuestionsAndAnswersDTO()) {
                    QuestionDTO questionDTO = map.get(staticContesQuestionsAndAnswerDTO.getQuestionId());
                    if (staticContesQuestionsAndAnswerDTO.getSubmittedAns() != null && staticContesQuestionsAndAnswerDTO.getSubmittedAns().length() > 0 && staticContesQuestionsAndAnswerDTO.getSubmittedAns().toLowerCase().equals(questionDTO.getAnswers().toLowerCase())) {

                        if (questionDTO.getDifficultyLevel().toLowerCase().equals("easy")) {
                            points += 1;
                            easyCount++;
                        } else if (questionDTO.getDifficultyLevel().toLowerCase().equals("medium")) {
                            points += 2;
                            mediumCount++;
                        } else if (questionDTO.getDifficultyLevel().toLowerCase().equals("difficult")) {
                            points += 3;
                            difficultCount++;
                        }
                        correctAnsCount++;
                    } else {
                        wrongAnsCount++;
                    }
                    totalTimeTaken += staticContesQuestionsAndAnswerDTO.getTimetaken();
                }
                staticContestReport.setPoints(points);
                staticContestReport.setEasyCount(easyCount);
                staticContestReport.setMediumCount(mediumCount);
                staticContestReport.setDifficultCount(difficultCount);
                staticContestReport.setCorrectAnsCount(correctAnsCount);
                staticContestReport.setWrongAnsCount(wrongAnsCount);
                staticContestReport.setTotalTimeTaken(totalTimeTaken);
                staticContestReportRepository.save(staticContestReport);

            }
            ReportStatus reportStatus = new ReportStatus();
            reportStatus.setContestId(contestId);
            reportStatus.setContestType(true);
            reportStatusRepository.save(reportStatus);
        }
        return true;
    }

    @Override
    public boolean addRank(String contestId) {
        List<StaticContestReportDTO> staticContestReportDTOS = staticContestReportRepository.findByContestIdOrderByPointsDescTotalTimeTakenAscCorrectAnsCountDescNoOfSkipsAsc(contestId);
        int rank = 0;
        for (StaticContestReportDTO obj : staticContestReportDTOS) {
            StaticContestReport staticContestReport = new StaticContestReport();
            BeanUtils.copyProperties(obj, staticContestReport);
            staticContestReport.setRank(rank++);
            staticContestReportRepository.save(staticContestReport);
        }
        return true;

    }
}
