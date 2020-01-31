package com.coviam.ScoringQuiz2020.controller;

import com.coviam.ScoringQuiz2020.dto.*;
import com.coviam.ScoringQuiz2020.service.DynamicContestSubmitService;
import com.coviam.ScoringQuiz2020.service.StaticContestSubmitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/contest")
public class ContestController {

    @Autowired
    StaticContestSubmitService staticContestSubmitService;

    @Autowired
    DynamicContestSubmitService dynamicContestSubmitService;

    @PostMapping(path = "/static/submit")
    public ResponseEntity<?> submitStaticContest(@Valid @RequestBody StaticContestSubmitDTO staticContestSubmitDTO) {
        boolean status = staticContestSubmitService.addStaticContestSubmitRecord(staticContestSubmitDTO);
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setStatus(status);
        if (status)
            return new ResponseEntity<>(statusDTO, HttpStatus.OK);
        return new ResponseEntity<>(statusDTO, HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping(path = "/dynamic/submit")
    public ResponseEntity<?> submitDynamicContest(@Valid @RequestBody DynamicContestSubmitDTO dynamicContestSubmitDTO) {
        boolean status = dynamicContestSubmitService.addDynamicContestSubmitRecord(dynamicContestSubmitDTO);
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setStatus(status);
        if (status)
            return new ResponseEntity<>(statusDTO, HttpStatus.OK);
        return new ResponseEntity<>(statusDTO, HttpStatus.NOT_ACCEPTABLE);
    }
}

