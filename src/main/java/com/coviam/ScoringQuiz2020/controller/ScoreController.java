package com.coviam.ScoringQuiz2020.controller;

import com.coviam.ScoringQuiz2020.dto.*;
import com.coviam.ScoringQuiz2020.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/contest")
public class ScoreController {

    @Autowired
    ScoreService scoreService;

    @PostMapping(path = "/static/submit")
    public ResponseEntity<?> submitStaticContest(@Valid @RequestBody StaticContestSubmitDTO staticContestSubmitDTO) {
        boolean status = scoreService.generatePointsForStaticContest(staticContestSubmitDTO);
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setStatus(status);
        if (status)
            return new ResponseEntity<>(statusDTO, HttpStatus.OK);
        return new ResponseEntity<>(statusDTO, HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping(path = "/dynamic/submit")
    public ResponseEntity<?> submitDynamicContest(@Valid @RequestBody DynamicContestSubmitDTO dynamicContestSubmitDTO) {
        boolean status = scoreService.generatePointsForDynamicContest(dynamicContestSubmitDTO);
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setStatus(status);
        if (status)
            return new ResponseEntity<>(statusDTO, HttpStatus.OK);
        return new ResponseEntity<>(statusDTO, HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping(path = "/get/report")
    public ResponseEntity<?> getStaticContestReportByContestId(@Valid @RequestBody ReportInputDTO reportInputDTO) {
        List<ReportOutputDTO> reportOutputDTOList = scoreService.getReport(reportInputDTO.getContestId(), reportInputDTO.getUserID(),reportInputDTO.isContestType());
        return new ResponseEntity<>(reportOutputDTOList, HttpStatus.OK);
    }

}

