package com.coviam.ScoringQuiz2020.service.impl;

import com.coviam.ScoringQuiz2020.document.DynamicContestReport;
import com.coviam.ScoringQuiz2020.document.ReportStatus;
import com.coviam.ScoringQuiz2020.document.StaticContestReport;
import com.coviam.ScoringQuiz2020.dto.*;
import com.coviam.ScoringQuiz2020.repository.*;
import com.coviam.ScoringQuiz2020.service.ReportStatusService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ReportStatusServiceImpl implements ReportStatusService {
    @Autowired
    ReportStatusRepository reportStatusRepository;

    @Autowired
    StaticContestSubmitRepository staticContestSubmitRepository;

    @Autowired
    StaticContestReportRepository staticContestReportRepository;

    @Autowired
    DynamicContestSubmitRepository dynamicContestSubmitRepository;

    @Autowired
    DynamicContestReportRepository dynamicContestReportRepository;


    @Override
    public boolean checkOrGenerateStaticContestReport(String contestId) {

        List<ReportStatus> reportStatusList = reportStatusRepository.findByContestIdAndContestType(contestId, true);
        if (reportStatusList != null && reportStatusList.size() > 0) {
            return true;
        } else {
            List<StaticContestSubmitDTO> staticContestSubmitDTOS = staticContestSubmitRepository.findByContestId(contestId);
            // https://api.myjson.com/bins/jdycq
            //TODO : get List<QuestionDTO> questions by contestId


            final String uri = "https://api.myjson.com/bins/181asq";
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<QUestionsDTO> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<QUestionsDTO>() {
                    });
            QUestionsDTO staticContestResultsDTO = responseEntity.getBody();
            System.out.println("======>" + staticContestResultsDTO.getQuestionDTOList());
            List<QuestionDTO> questionDTOS = staticContestResultsDTO.getQuestionDTOList();
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

                if (obj.getNoOfSkips() == 0) {
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
    public boolean addRankForStaticContest(String contestId) {
        List<StaticContestReport> staticContestReportDTOS = staticContestReportRepository.findByContestIdOrderByPointsDescTotalTimeTakenAscCorrectAnsCountDescNoOfSkipsAsc(contestId);
        int rank = 1;
        for (StaticContestReport obj : staticContestReportDTOS) {
            obj.setRank(rank++);
            staticContestReportRepository.save(obj);
        }
        return true;

    }

    @Override
    public boolean checkOrGenerateDynamicContestReport(String contestId) {
        List<ReportStatus> reportStatusList = reportStatusRepository.findByContestIdAndContestType(contestId, false);

        if (reportStatusList != null && reportStatusList.size() > 0) {
            return true;
        } else {

            //TODO : get List<QuestionDTO> questions by contestId
            List<QuestionDTO> questionDTOS = new ArrayList<QuestionDTO>();
            for (QuestionDTO questionDTO : questionDTOS) {
                Date date = new Date();
                int points = 105;
                List<DynamicContestSubmitDTO> dynamicContestSubmitDTOS = dynamicContestSubmitRepository.findByContestIdAndQuestionIdAndSubmittedAnsOrderByDateAndTimeAsc(contestId, questionDTO.getQuestionId(), questionDTO.getAnswers());
                for (DynamicContestSubmitDTO obj : dynamicContestSubmitDTOS) {
                    List<DynamicContestReportDTO> dynamicContestReportDTOS = dynamicContestReportRepository.findByContestIdAndUserId(contestId, obj.getUserId());
                    if (obj.getDateAndTime() != date) {
                        if (points != 0) {
                            points -= 5;
                        }
                        date = obj.getDateAndTime();
                    }
                    if (dynamicContestReportDTOS != null && dynamicContestReportDTOS.size() > 0) {
                        DynamicContestReport dynamicContestReport = new DynamicContestReport();
                        BeanUtils.copyProperties(dynamicContestReportDTOS.get(0), dynamicContestReport);
                        dynamicContestReport.setCorrectAnsCount(dynamicContestReport.getCorrectAnsCount() + 1);
                        if (questionDTO.getDifficultyLevel().toLowerCase().equals("easy"))
                            dynamicContestReport.setEasyCount(dynamicContestReport.getEasyCount() + 1);
                        else if (questionDTO.getDifficultyLevel().toLowerCase().equals("medium"))
                            dynamicContestReport.setMediumCount(dynamicContestReport.getMediumCount() + 1);
                        else if (questionDTO.getDifficultyLevel().toLowerCase().equals("difficult"))
                            dynamicContestReport.setDifficultCount(dynamicContestReport.getDifficultCount() + 1);
                        dynamicContestReport.setPoints(points);
                        dynamicContestReportRepository.save(dynamicContestReport);
                    } else {

                        DynamicContestReport dynamicContestReport = new DynamicContestReport();
                        BeanUtils.copyProperties(obj, dynamicContestReport);
                        dynamicContestReport.setCorrectAnsCount(1);
                        if (questionDTO.getDifficultyLevel().toLowerCase().equals("easy"))
                            dynamicContestReport.setEasyCount(1);
                        else if (questionDTO.getDifficultyLevel().toLowerCase().equals("medium"))
                            dynamicContestReport.setMediumCount(1);
                        else if (questionDTO.getDifficultyLevel().toLowerCase().equals("difficult"))
                            dynamicContestReport.setDifficultCount(1);
                        dynamicContestReport.setPoints(points);
                        dynamicContestReportRepository.save(dynamicContestReport);
                    }

                }
            }
        }
        return false;
    }

    @Override
    public boolean addRankForDynamicContest(String contestId) {

        List<DynamicContestReportDTO> dynamicContestReportDTOS = dynamicContestReportRepository.findByContestIdOrderByPointsDesc(contestId);
        int rank = 0;
        int points = -1;
        boolean change = true;
        for (DynamicContestReportDTO obj : dynamicContestReportDTOS) {
            if (obj.getPoints() != points) {
                rank++;
                points = obj.getPoints();
            }
            DynamicContestReport dynamicContestReport = new DynamicContestReport();
            BeanUtils.copyProperties(obj, dynamicContestReport);
            dynamicContestReport.setRank(rank);
            dynamicContestReportRepository.save(dynamicContestReport);
        }
        return true;
    }
}
