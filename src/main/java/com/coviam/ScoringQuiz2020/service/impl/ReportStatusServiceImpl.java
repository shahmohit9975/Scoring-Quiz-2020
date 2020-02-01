package com.coviam.ScoringQuiz2020.service.impl;

import com.coviam.ScoringQuiz2020.document.DynamicContestReport;
import com.coviam.ScoringQuiz2020.document.ReportStatus;
import com.coviam.ScoringQuiz2020.document.StaticContestReport;
import com.coviam.ScoringQuiz2020.dto.*;
import com.coviam.ScoringQuiz2020.repository.*;
import com.coviam.ScoringQuiz2020.service.ReportStatusService;
import com.coviam.ScoringQuiz2020.service.UserRecordsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    UserRecordsService userRecordsService;

    @Value("${spring.staticContest.questionAnsDTO.url}")
    private String staticContestUri;
    @Value("${spring.dynamicContest.questionAnsDTO.url}")
    private String dynamicContestUri;
    @Value("${spring.contest.mostAnsweredQuestionCount.url}")
    private String mostAnsweredQuestionCountUri;
    @Value("${spring.contest.userRecords.url}")
    private String userRecordsUri;

    @Override
    public boolean checkOrGenerateStaticContestReport(String contestId) {

        List<ReportStatus> reportStatusList = reportStatusRepository.findByContestIdAndContestType(contestId, true);
        if (reportStatusList != null && reportStatusList.size() > 0) {
            return true;
        } else {
            List<StaticContestSubmitDTO> staticContestSubmitDTOS = staticContestSubmitRepository.findByContestId(contestId);
            //TODO : get List<QuestionDTO> questions by contestId
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<List<QuestionDTO>> responseEntity = restTemplate.exchange(
                    staticContestUri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<QuestionDTO>>() {
                    });
            List<QuestionDTO> questionDTOS = responseEntity.getBody();
            Map<String, Integer> correctAnswerQuestionIds = new HashMap<String, Integer>();
            Map<String, QuestionDTO> map = new HashMap<String, QuestionDTO>();
            List<UserRecordsDTO> userRecordsDTOS = new ArrayList<UserRecordsDTO>();
            for (QuestionDTO questionDTO : questionDTOS) {
                map.put(questionDTO.getQuestionId(), questionDTO);
                correctAnswerQuestionIds.put(questionDTO.getQuestionId(), 0);
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

                        correctAnswerQuestionIds.put(staticContesQuestionsAndAnswerDTO.getQuestionId(), correctAnswerQuestionIds.get(staticContesQuestionsAndAnswerDTO.getQuestionId()) + 1);
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
                UserRecordsDTO userRecordsDTO = new UserRecordsDTO();
                userRecordsDTO.setUserId(obj.getUserId());
                userRecordsDTO.setCorrectAnswer(correctAnsCount);
                userRecordsDTO.setEasy(easyCount);
                userRecordsDTO.setMedium(mediumCount);
                userRecordsDTO.setDifficult(difficultCount);
                userRecordsDTO.setPoints(points);

                staticContestReport.setPoints(points);
                staticContestReport.setEasyCount(easyCount);
                staticContestReport.setMediumCount(mediumCount);
                staticContestReport.setDifficultCount(difficultCount);
                staticContestReport.setCorrectAnsCount(correctAnsCount);
                staticContestReport.setWrongAnsCount(wrongAnsCount);
                staticContestReport.setTotalTimeTaken(totalTimeTaken);
                staticContestReportRepository.save(staticContestReport);

                userRecordsDTOS.add(userRecordsDTO);

            }
            ReportStatus reportStatus = new ReportStatus();
            reportStatus.setContestId(contestId);
            reportStatus.setContestType(true);
            reportStatusRepository.save(reportStatus);
            // todo send map to questions microservice
            //correctAnswerQuestionIds;
//            RestTemplate restTemplate1 = new RestTemplate();
//            StatusDTO responseMessage = restTemplate1.postForObject(
//                    mostAnsweredQuestionCountUri, correctAnswerQuestionIds, StatusDTO.class
//            );
            //***************
//            RestTemplate restTemplate2 = new RestTemplate();
//            StatusDTO responseMessage = restTemplate2.postForObject(
//                    userRecordsUri, userRecordsDTOS, StatusDTO.class
//            );
            userRecordsService.updateRecords(userRecordsDTOS);
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
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<List<QuestionDTO>> responseEntity = restTemplate.exchange(
                    dynamicContestUri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<QuestionDTO>>() {
                    });
            List<QuestionDTO> questionDTOS = responseEntity.getBody();
            Map<String, Integer> correctAnswerQuestionIds = new HashMap<String, Integer>();
            List<UserRecordsDTO> userRecordsDTOS = new ArrayList<UserRecordsDTO>();
            for (QuestionDTO questionDTO : questionDTOS) {
                correctAnswerQuestionIds.put(questionDTO.getQuestionId(), 0);
                Date date = new Date();
                int points = 105;
                List<DynamicContestSubmitDTO> dynamicContestSubmitDTOS = dynamicContestSubmitRepository.findByContestIdAndQuestionIdAndSubmittedAnsOrderByDateAndTimeAsc(contestId, questionDTO.getQuestionId(), questionDTO.getAnswers());

                for (DynamicContestSubmitDTO obj : dynamicContestSubmitDTOS) {

                    UserRecordsDTO userRecordsDTO = new UserRecordsDTO();
                    userRecordsDTO.setUserId(obj.getUserId());
                    userRecordsDTO.setCorrectAnswer(1);
                    List<DynamicContestReportDTO> dynamicContestReportDTOS = dynamicContestReportRepository.findByContestIdAndUserId(contestId, obj.getUserId());
                    correctAnswerQuestionIds.put(obj.getQuestionId(), correctAnswerQuestionIds.get(obj.getQuestionId()) + 1);
                    if (obj.getDateAndTime() != date) {
                        if (points != 0) {
                            points -= 5;
                        }
                        date = obj.getDateAndTime();
                    }
                    if (dynamicContestReportDTOS != null && dynamicContestReportDTOS.size() > 0) {
                        DynamicContestReport dynamicContestReport = dynamicContestReportRepository.findById(dynamicContestReportDTOS.get(0).get_id()).get();
                        dynamicContestReport.setCorrectAnsCount(dynamicContestReport.getCorrectAnsCount() + 1);
                        if (questionDTO.getDifficultyLevel().toLowerCase().equals("easy")) {
                            userRecordsDTO.setEasy(1);
                            dynamicContestReport.setEasyCount(dynamicContestReport.getEasyCount() + 1);
                        } else if (questionDTO.getDifficultyLevel().toLowerCase().equals("medium")) {
                            userRecordsDTO.setMedium(1);
                            dynamicContestReport.setMediumCount(dynamicContestReport.getMediumCount() + 1);
                        } else if (questionDTO.getDifficultyLevel().toLowerCase().equals("difficult")) {
                            userRecordsDTO.setDifficult(1);
                            dynamicContestReport.setDifficultCount(dynamicContestReport.getDifficultCount() + 1);
                        }
                        dynamicContestReport.setPoints(dynamicContestReport.getPoints() + points);
                        userRecordsDTO.setPoints(points);
                        dynamicContestReportRepository.save(dynamicContestReport);
                    } else {
                        DynamicContestReport dynamicContestReport = new DynamicContestReport();
                        BeanUtils.copyProperties(obj, dynamicContestReport);
                        dynamicContestReport.setCorrectAnsCount(1);
                        if (questionDTO.getDifficultyLevel().toLowerCase().equals("easy")) {
                            userRecordsDTO.setEasy(1);
                            dynamicContestReport.setEasyCount(1);
                        } else if (questionDTO.getDifficultyLevel().toLowerCase().equals("medium")) {
                            dynamicContestReport.setMediumCount(1);
                            userRecordsDTO.setMedium(1);
                        } else if (questionDTO.getDifficultyLevel().toLowerCase().equals("difficult")) {
                            userRecordsDTO.setDifficult(1);
                            dynamicContestReport.setDifficultCount(1);
                        }
                        dynamicContestReport.setPoints(points);
                        userRecordsDTO.setPoints(points);
                        dynamicContestReportRepository.save(dynamicContestReport);
                    }
                    userRecordsDTOS.add(userRecordsDTO);
                }
            }
            ReportStatus reportStatus = new ReportStatus();
            reportStatus.setContestId(contestId);
            reportStatus.setContestType(false);
            reportStatusRepository.save(reportStatus);
            // todo send map to questions microservice
            //correctAnswerQuestionIds;
//            RestTemplate restTemplate1 = new RestTemplate();
//            StatusDTO responseMessage = restTemplate1.postForObject(
//                    mostAnsweredQuestionCountUri, correctAnswerQuestionIds, StatusDTO.class
//            );
            //***************
//            RestTemplate restTemplate2 = new RestTemplate();
//            StatusDTO responseMessage = restTemplate2.postForObject(
//                    userRecordsUri, userRecordsDTOS, StatusDTO.class
//            );
            userRecordsService.updateRecords(userRecordsDTOS);
        }
        return true;
    }

    @Override
    public boolean addRankForDynamicContest(String contestId) {
        List<DynamicContestReport> dynamicContestReportList = dynamicContestReportRepository.findByContestIdOrderByPointsDesc(contestId);
        int rank = 0;
        int points = -1;
        boolean change = true;
        for (DynamicContestReport obj : dynamicContestReportList) {
            if (obj.getPoints() != points) {
                rank++;
                points = obj.getPoints();
            }
            obj.setRank(rank);
            dynamicContestReportRepository.save(obj);
        }
        return true;
    }
}
