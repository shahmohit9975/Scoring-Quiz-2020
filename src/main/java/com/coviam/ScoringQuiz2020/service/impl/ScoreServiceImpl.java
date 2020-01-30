package com.coviam.ScoringQuiz2020.service.impl;

import com.coviam.ScoringQuiz2020.document.Scoring;
import com.coviam.ScoringQuiz2020.dto.*;
import com.coviam.ScoringQuiz2020.repository.ScoreRepository;
import com.coviam.ScoringQuiz2020.service.ScoreService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {
    @Autowired
    ScoreRepository scoreRepository;

    private static DynamicContestResultsDTO dynamicContestResultsDTO = null;
    private static String originalAnswer;
    private static String dynamicQuestionChange = "";
    private static int dynamicPoins = 100;

    // call api to get StaticContestResultsDTO
    // correct -> 1
    // correct Average -> +1
    // correct Hard -> +2
    // without skipping -> +5


    @Override
    public boolean generatePointsForStaticContest(StaticContestSubmitDTO staticContestSubmitDTO) {

        String contestId = staticContestSubmitDTO.getContestId();
        String userId = staticContestSubmitDTO.getUserID();
        int timetaken = 0;
        int easyQuestion = 0;
        int mediumQuestion = 0;
        int difficultQuestion = 0;
        int correctAnswer = 0;
        int wrongAnswer = 0;
        int points = 0;
        List<Scoring> listObj = scoreRepository.findByUserIDAndContestIdAndContestType(userId, contestId, true);

        int userNoOfSkips = staticContestSubmitDTO.getNoOfskips();
//===================================================================================
        final String uri = "https://api.myjson.com/bins/xakqa";
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<StaticContestResultsDTO> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<StaticContestResultsDTO>() {
                });
        StaticContestResultsDTO staticContestResultsDTO = responseEntity.getBody();
        if (listObj == null || listObj.size() == 0) {
//            Scoring scoringObj = listObj.get(0);

//===================================================================================
//            final String uri = "https://api.myjson.com/bins/xakqa";
//            RestTemplate restTemplate = new RestTemplate();
//
//            ResponseEntity<StaticContestResultsDTO> responseEntity = restTemplate.exchange(
//                    uri,
//                    HttpMethod.GET,
//                    null,
//                    new ParameterizedTypeReference<StaticContestResultsDTO>() {
//                    });
//            StaticContestResultsDTO allCartDetailsDTO = responseEntity.getBody();
//===================================================================================

            //TODO call static contest microservice
//            StaticContestResultsDTO staticContestResultsDTO = new StaticContestResultsDTO();
//            StaticContestResultsDTO staticContestResultsDTO = responseEntity.getBody();
            System.out.println("============>" + staticContestResultsDTO.toString());
            if (userNoOfSkips == 0) {
                points += 5;

            }
            int index = staticContestSubmitDTO.getStarting();
            System.out.println("submittedQuestionsDTO size : " + staticContestSubmitDTO.getQuestionAnsDTOList().size());
            int counter = 0;
            int totalSubmittedQuestionsLength = staticContestSubmitDTO.getQuestionAnsDTOList().size();
            for (int no = index - 1; no < totalSubmittedQuestionsLength; no++) {
                QuestionsDTO originalQuestionsDTO = staticContestResultsDTO.getQuestionsDTOList().get(no);

                QuestionAnsDTO submittedQuestionsDTO = staticContestSubmitDTO.getQuestionAnsDTOList().get(counter++);
                System.out.println("****** submittedQuestionsDTO ********** > " + submittedQuestionsDTO.toString());
                String originalAnswer = originalQuestionsDTO.getAnswers().toLowerCase();
                String submittedAnswer = submittedQuestionsDTO.getAnswer().toLowerCase();
                timetaken += submittedQuestionsDTO.getTimetaken();
                if (submittedAnswer != null && submittedAnswer.length() > 0 && originalAnswer.equals(submittedAnswer)) {
                    if (originalQuestionsDTO.getDifficultyLevel().toLowerCase().equals("easy")) {
                        points += 1;
                        easyQuestion++;
                        correctAnswer++;
                    } else if (originalQuestionsDTO.getDifficultyLevel().toLowerCase().equals("medium")) {
                        points += 2;
                        mediumQuestion++;
                        correctAnswer++;
                    } else if (originalQuestionsDTO.getDifficultyLevel().toLowerCase().equals("difficult")) {
                        points += 3;
                        difficultQuestion++;
                        correctAnswer++;
                    } else {
                        wrongAnswer++;
                    }
                }
            }
            Scoring scoring = new Scoring();
            scoring.setContestId(contestId);
            scoring.setContestType(true);
            scoring.setPoints(points);
            scoring.setUserID(userId);
            scoring.setTimetaken(timetaken);
            scoring.setNoOfSkips(userNoOfSkips);
            scoring.setCorrectAnswer(correctAnswer);
            scoring.setWrongAnswer(wrongAnswer);
            scoreRepository.save(scoring);
            //TODO call user document for add questions counts
            return true;
        }
        Scoring scoringObj = listObj.get(0);
        int storedPoints = scoringObj.getPoints();
        int storedNoOfSkips = scoringObj.getNoOfSkips();
        int storedCorrectAnswer = scoringObj.getCorrectAnswer();
        int storedWrongAnswer = scoringObj.getWrongAnswer();
        int storedTimetaken = scoringObj.getTimetaken();
        if (storedNoOfSkips == 0 && userNoOfSkips > 0) {
            points -= 5;
        }
        storedNoOfSkips += userNoOfSkips;
//        StaticContestResultsDTO staticContestResultsDTO = new StaticContestResultsDTO();
        int index = staticContestSubmitDTO.getStarting();
        int counter = 0;
        int totalSubmittedQuestionsLength = staticContestSubmitDTO.getQuestionAnsDTOList().size();
        for (int no = index - 1; no <= totalSubmittedQuestionsLength; no++) {
            QuestionsDTO originalQuestionsDTO = staticContestResultsDTO.getQuestionsDTOList().get(no);
            QuestionAnsDTO submittedQuestionsDTO = staticContestSubmitDTO.getQuestionAnsDTOList().get(counter++);
            String originalAnswer = originalQuestionsDTO.getAnswers().toLowerCase();
            String submittedAnswer = submittedQuestionsDTO.getAnswer().toLowerCase();
            timetaken += submittedQuestionsDTO.getTimetaken();
            if (submittedAnswer != null && submittedAnswer.length() > 0 && originalAnswer.equals(submittedAnswer)) {
                if (originalQuestionsDTO.getDifficultyLevel().toLowerCase().equals("easy")) {
                    points += 1;
                    easyQuestion++;
                    correctAnswer++;
                } else if (originalQuestionsDTO.getDifficultyLevel().toLowerCase().equals("medium")) {
                    points += 2;
                    mediumQuestion++;
                    correctAnswer++;
                } else if (originalQuestionsDTO.getDifficultyLevel().toLowerCase().equals("difficult")) {
                    points += 3;
                    difficultQuestion++;
                    correctAnswer++;
                } else {
                    wrongAnswer++;
                }
            }
        }

        scoringObj.setPoints(storedPoints + points);
        scoringObj.setNoOfSkips(storedNoOfSkips);
        scoringObj.setWrongAnswer(storedWrongAnswer + wrongAnswer);
        scoringObj.setCorrectAnswer(storedCorrectAnswer + correctAnswer);
        scoringObj.setTimetaken(storedTimetaken + timetaken);
        scoreRepository.save(scoringObj);
        //TODO call user document for add questions counts
        return true;
    }

    @Override
    public boolean generatePointsForDynamicContest(DynamicContestSubmitDTO dynamicContestSubmitDTO) {
        return generatePointsForDynamicContestSynchronized(dynamicContestSubmitDTO);
    }


    private synchronized boolean generatePointsForDynamicContestSynchronized(DynamicContestSubmitDTO dynamicContestSubmitDTO) {

        System.out.println("dynamicQuestionChange :" + dynamicQuestionChange + " -- " + dynamicContestSubmitDTO.getQuestionId());
        if (!dynamicQuestionChange.equals(dynamicContestSubmitDTO.getQuestionId())) {
            System.out.println("inside ifff");
            dynamicQuestionChange = dynamicContestSubmitDTO.getQuestionId();
            dynamicPoins = 100;
            //TODO call static contest microservice
            final String uri = "https://api.myjson.com/bins/1bh4cy";
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<DynamicContestResultsDTO> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<DynamicContestResultsDTO>() {
                    });
//                StaticContestResultsDTO staticContestResultsDTO = responseEntity.getBody();
//                dynamicContestResultsDTO = new DynamicContestResultsDTO();
            dynamicContestResultsDTO = responseEntity.getBody();
            originalAnswer = dynamicContestResultsDTO.getAnswer().toLowerCase();
        }
        String contestId = dynamicContestSubmitDTO.getContestId();
        String userId = dynamicContestSubmitDTO.getUserID();
        String submittedAnswer = dynamicContestSubmitDTO.getAnswer().toLowerCase();
        int correctAnswer = 0;
        int wrongAnswer = 0;
        int points = 0;
        List<Scoring> listObj = scoreRepository.findByUserIDAndContestIdAndContestType(userId, contestId, false);

        if (listObj == null || listObj.size() == 0) {

            System.out.println("submittedAnswer : " + submittedAnswer + " => originalAnswer : " + originalAnswer);
            if (submittedAnswer != null && submittedAnswer.length() > 0 && originalAnswer.equals(submittedAnswer)) {
                System.out.println("IF");
                Scoring scoring = new Scoring();
                scoring.setCorrectAnswer(1);
                scoring.setWrongAnswer(0);
                scoring.setUserID(userId);
                scoring.setPoints(dynamicPoins);
                scoring.setContestType(false);
                scoring.setContestId(contestId);
                scoreRepository.save(scoring);
                if (dynamicPoins != 5) {
                    dynamicPoins -= 5;
                }
            } else {
                System.out.println("ELSE");
                Scoring scoring = new Scoring();
                scoring.setCorrectAnswer(0);
                scoring.setWrongAnswer(1);
                scoring.setUserID(userId);
                scoring.setPoints(0);
                scoring.setContestType(false);
                scoring.setContestId(contestId);
                scoreRepository.save(scoring);

            }
            //TODO call user document for add questions counts
            return true;
        }
        Scoring scoringObj = listObj.get(0);
        if (submittedAnswer != null && submittedAnswer.length() > 0 && originalAnswer.equals(submittedAnswer)) {
            scoringObj.setCorrectAnswer(scoringObj.getCorrectAnswer() + 1);
            scoringObj.setPoints(scoringObj.getPoints() + dynamicPoins);
            scoringObj.setContestType(false);
            scoringObj.setContestId(contestId);
            scoreRepository.save(scoringObj);
            if (dynamicPoins != 5) {
                dynamicPoins -= 5;
            }
        } else {
            scoringObj.setWrongAnswer(scoringObj.getWrongAnswer() + 1);
            scoringObj.setContestType(false);
            scoringObj.setContestId(contestId);
        }
        //TODO call user document for add questions counts
        return true;

    }

    @Override
    public List<ReportOutputDTO> getReport(String contestId, String userID, boolean contestType) {
        List<Scoring> top10ScoringList = scoreRepository.findTop10ByContestIdAndContestTypeOrderByPointsDescTimetakenAsc(contestId, contestType);
        List<ReportOutputDTO> reportOutputDTOList = new ArrayList<ReportOutputDTO>();
        int rank = 1;
        for (Scoring scoring : top10ScoringList) {
            ReportOutputDTO reportOutputDTO = new ReportOutputDTO();
            BeanUtils.copyProperties(scoring, reportOutputDTO);
            reportOutputDTO.setRank(rank++);
            reportOutputDTOList.add(reportOutputDTO);
        }

        final List<Scoring> byContestIdAndContestType = scoreRepository.findByContestIdAndContestTypeOrderByPointsDescTimetakenAsc(contestId, contestType);
        rank = 0;
        for (Scoring scoring : byContestIdAndContestType) {
            rank++;
            if (scoring.getUserID().equals(userID)) {
                ReportOutputDTO reportOutputDTO = new ReportOutputDTO();
                BeanUtils.copyProperties(scoring, reportOutputDTO);
                reportOutputDTO.setRank(rank);
                reportOutputDTOList.add(reportOutputDTO);
            }
        }
        return reportOutputDTOList;
    }
}
