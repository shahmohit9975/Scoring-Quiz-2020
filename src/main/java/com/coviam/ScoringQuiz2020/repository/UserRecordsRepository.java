package com.coviam.ScoringQuiz2020.repository;

import com.coviam.ScoringQuiz2020.document.UserRecords;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRecordsRepository extends MongoRepository<UserRecords, String> {
}
