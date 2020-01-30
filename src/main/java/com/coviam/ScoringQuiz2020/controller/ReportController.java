package com.coviam.ScoringQuiz2020.controller;

import com.coviam.ScoringQuiz2020.document.StaticContestReport;
import com.coviam.ScoringQuiz2020.dto.ReportInputDTO;
import com.coviam.ScoringQuiz2020.dto.ReportOutputDTO;
import com.coviam.ScoringQuiz2020.dto.StaticContestReportDTO;
import com.coviam.ScoringQuiz2020.service.StaticContestReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/report")
public class ReportController {


    @Autowired
    StaticContestReportService staticContestReportService;

    @PostMapping(path = "/static/get")
    public ResponseEntity<?> getStaticContestReport(@Valid @RequestBody ReportInputDTO reportInputDTO) {
        List<StaticContestReportDTO> StaticContestReportDTO = staticContestReportService.getReport(reportInputDTO.getContestId(), reportInputDTO.getUserID());
        return new ResponseEntity<>(StaticContestReportDTO, HttpStatus.OK);
    }
}
