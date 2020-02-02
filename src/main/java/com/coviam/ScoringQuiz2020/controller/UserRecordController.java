package com.coviam.ScoringQuiz2020.controller;

import com.coviam.ScoringQuiz2020.dto.UserRecordsDTO;
import com.coviam.ScoringQuiz2020.service.UserRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/userRecord")
public class UserRecordController {
    @Autowired
    UserRecordsService userRecordsService;

    @GetMapping(path = "/get/{userId}")
    public ResponseEntity<?> getUserRecords(@PathVariable(value = "userId") String userId) {
        UserRecordsDTO userRecordsDTO = userRecordsService.getRecords(userId);
        return new ResponseEntity<>(userRecordsDTO, HttpStatus.OK);
    }
}
