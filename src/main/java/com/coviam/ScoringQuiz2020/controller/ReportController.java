package com.coviam.ScoringQuiz2020.controller;

import com.coviam.ScoringQuiz2020.document.StaticContestReport;
import com.coviam.ScoringQuiz2020.dto.DynamicContestReportDTO;
import com.coviam.ScoringQuiz2020.dto.ReportInputDTO;
import com.coviam.ScoringQuiz2020.dto.ReportOutputDTO;
import com.coviam.ScoringQuiz2020.dto.StaticContestReportDTO;
import com.coviam.ScoringQuiz2020.service.DynamicContestReportService;
import com.coviam.ScoringQuiz2020.service.StaticContestReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@CrossOrigin(origins = "*",allowedHeaders = "*")
@RestController
@RequestMapping("/report")
public class ReportController {


    @Autowired
    StaticContestReportService staticContestReportService;
    @Autowired
    DynamicContestReportService dynamicContestReportService;

    @PostMapping(path = "/static/user/get")
    public ResponseEntity<?> getStaticContestUserReport(@Valid @RequestBody ReportInputDTO reportInputDTO) {
        List<StaticContestReportDTO> StaticContestReportDTO = staticContestReportService.getReportForUser(reportInputDTO.getContestId(), reportInputDTO.getUserId());
        return new ResponseEntity<>(StaticContestReportDTO, HttpStatus.OK);
    }

    @PostMapping(path = "/static/master/get")
    public ResponseEntity<?> getStaticContesMastertReport(@Valid @RequestBody ReportInputDTO reportInputDTO) {
        List<StaticContestReportDTO> StaticContestReportDTO = staticContestReportService.getReportForMaster(reportInputDTO.getContestId());
        return new ResponseEntity<>(StaticContestReportDTO, HttpStatus.OK);
    }

    @PostMapping(path = "/dynamic/user/get")
    public ResponseEntity<?> getDynamicContestUserReport(@Valid @RequestBody ReportInputDTO reportInputDTO) {
        List<DynamicContestReportDTO> dynamicContestReportDTOS = dynamicContestReportService.getReportForUser(reportInputDTO.getContestId(), reportInputDTO.getUserId());
        return new ResponseEntity<>(dynamicContestReportDTOS, HttpStatus.OK);
    }

    @PostMapping(path = "/dynamic/master/get")
    public ResponseEntity<?> getDynamicContesMastertReport(@Valid @RequestBody ReportInputDTO reportInputDTO) {
        List<DynamicContestReportDTO> dynamicContestReportDTOS = dynamicContestReportService.getReportForMaster(reportInputDTO.getContestId());
        return new ResponseEntity<>(dynamicContestReportDTOS, HttpStatus.OK);
    }
}
