package com.coviam.ScoringQuiz2020;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ScoringQuiz2020Application {

    public static void main(String[] args) {
        SpringApplication.run(ScoringQuiz2020Application.class, args);
    }

}
