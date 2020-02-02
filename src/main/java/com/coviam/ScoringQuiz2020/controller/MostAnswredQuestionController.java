package com.coviam.ScoringQuiz2020.controller;

import com.coviam.ScoringQuiz2020.service.MostAnswredQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mostAnswredQuestion")
public class MostAnswredQuestionController {
    @Autowired
    MostAnswredQuestionService mostAnswredQuestionService;

    @GetMapping(path = "/get")
    public ResponseEntity<?> getMostAnswredQuestionList() {

        return new ResponseEntity<>(mostAnswredQuestionService.getAllRecords(), HttpStatus.OK);
    }
}
