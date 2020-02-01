package com.coviam.ScoringQuiz2020.controller;

import com.coviam.ScoringQuiz2020.dto.GenerateReportDTO;
import com.coviam.ScoringQuiz2020.dto.StatusDTO;
import com.coviam.ScoringQuiz2020.service.ReportStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*",allowedHeaders = "*")
@RestController
@RequestMapping("/reportStatus")
public class ReportStatusController {

    @Autowired
    ReportStatusService reportStatusService;

    @PostMapping(path = "/static")
    public ResponseEntity<?> checkOrGenerateStaticContestReport(@Valid @RequestBody GenerateReportDTO generateReportDTO) {
        boolean status1 = reportStatusService.checkOrGenerateStaticContestReport(generateReportDTO.getContestId());
        boolean status2 = reportStatusService.addRankForStaticContest(generateReportDTO.getContestId());
        StatusDTO statusDTO = new StatusDTO();
        if (status1 && status2) {
            statusDTO.setStatus(true);
        }
        return new ResponseEntity<>(statusDTO, HttpStatus.OK);
    }

    @PostMapping(path = "/dynamic")
    public ResponseEntity<?> checkOrGenerateDynamicContestReport(@Valid @RequestBody GenerateReportDTO generateReportDTO) {
        boolean status1 = reportStatusService.checkOrGenerateDynamicContestReport(generateReportDTO.getContestId());
        boolean status2 = reportStatusService.addRankForDynamicContest(generateReportDTO.getContestId());
        System.out.println(status1);
        System.out.println(status2);
        StatusDTO statusDTO = new StatusDTO();
        if (status1 && status2) {
            statusDTO.setStatus(true);
        }
        return new ResponseEntity<>(statusDTO, HttpStatus.OK);
    }
}
