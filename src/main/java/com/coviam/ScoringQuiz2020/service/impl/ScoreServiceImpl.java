package com.coviam.ScoringQuiz2020.service.impl;

import com.coviam.ScoringQuiz2020.document.Scoring;
import com.coviam.ScoringQuiz2020.dto.*;
import com.coviam.ScoringQuiz2020.repository.ScoreRepository;
import com.coviam.ScoringQuiz2020.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {
    @Autowired
    ScoreRepository scoreRepository;
    // call api to get StaticContestResultsDTO

    // correct -> 1
    // correct Average -> +1
    // correct Hard -> +2
    // without skipping -> +5


    @Override
    public boolean generatePointsForStaticContest(StaticContestSubmitDTO staticContestSubmitDTO) {

        String contestId = staticContestSubmitDTO.getContestId();
        String userId = staticContestSubmitDTO.getUserID();
        int easyQuestion = 0;
        int mediumQuestion = 0;
        int difficultQuestion = 0;
        int correctAnswer = 0;
        int wrongAnswer = 0;
        int points = 0;
        List<Scoring> listObj = scoreRepository.findByUserIDAndContestIdAndContestType(userId, contestId, true);

        int userNoOfSkips = staticContestSubmitDTO.getNoOfskips();

        if (listObj == null || listObj.size() == 0) {
            Scoring scoringObj = listObj.get(0);
            //TODO call static contest microservice
            StaticContestResultsDTO staticContestResultsDTO = new StaticContestResultsDTO();
            if (userNoOfSkips == 0) {
                points += 5;

            }
            int index = staticContestSubmitDTO.getStarting();
            int counter = 0;
            int totalSubmittedQuestionsLength = staticContestSubmitDTO.getQuestionAnsDTOList().size();
            for (int no = index - 1; no < totalSubmittedQuestionsLength; no++) {
                QuestionsDTO originalQuestionsDTO = staticContestResultsDTO.getQuestionsDTOList().get(no);

                QuestionAnsDTO submittedQuestionsDTO = staticContestSubmitDTO.getQuestionAnsDTOList().get(counter++);

                String originalAnswer = originalQuestionsDTO.getAnswers().toLowerCase();
                String submittedAnswer = submittedQuestionsDTO.getAnswer().toLowerCase();

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

        if (storedNoOfSkips == 0 && userNoOfSkips > 0) {
            points -= 5;
        }
        storedNoOfSkips += userNoOfSkips;
        StaticContestResultsDTO staticContestResultsDTO = new StaticContestResultsDTO();
        int index = staticContestSubmitDTO.getStarting();
        int counter = 0;
        int totalSubmittedQuestionsLength = staticContestSubmitDTO.getQuestionAnsDTOList().size();
        for (int no = index - 1; no < totalSubmittedQuestionsLength; no++) {
            QuestionsDTO originalQuestionsDTO = staticContestResultsDTO.getQuestionsDTOList().get(no);
            QuestionAnsDTO submittedQuestionsDTO = staticContestSubmitDTO.getQuestionAnsDTOList().get(counter++);
            String originalAnswer = originalQuestionsDTO.getAnswers().toLowerCase();
            String submittedAnswer = submittedQuestionsDTO.getAnswer().toLowerCase();
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
        scoreRepository.save(scoringObj);
        //TODO call user document for add questions counts
        return true;
    }

    private static DynamicContestResultsDTO dynamicContestResultsDTO = null;
    private static String submittedAnswer;
    private static String originalAnswer;
    static String dynamicQuestionChange = "";
    static int dynamicPoins = 100;

    @Override
    public boolean generatePointsForDynamicContest(DynamicContestSubmitDTO dynamicContestSubmitDTO) {

        synchronized (this) {
            if (!dynamicQuestionChange.equals(dynamicContestSubmitDTO.getQuestionId())) {
                dynamicQuestionChange = dynamicContestSubmitDTO.getQuestionId();
                dynamicPoins = 100;
                //TODO call static contest microservice
                dynamicContestResultsDTO = new DynamicContestResultsDTO();
                submittedAnswer = dynamicContestSubmitDTO.getAnswer().toLowerCase();
                originalAnswer = dynamicContestResultsDTO.getAnswers().toLowerCase();
            }
            String contestId = dynamicContestSubmitDTO.getContestId();
            String userId = dynamicContestSubmitDTO.getUserID();
            int correctAnswer = 0;
            int wrongAnswer = 0;
            int points = 0;
            List<Scoring> listObj = scoreRepository.findByUserIDAndContestIdAndContestType(userId, contestId, false);

            if (listObj == null || listObj.size() == 0) {

                if (submittedAnswer != null && submittedAnswer.length() > 0 && originalAnswer.equals(submittedAnswer)) {
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
    }
}
