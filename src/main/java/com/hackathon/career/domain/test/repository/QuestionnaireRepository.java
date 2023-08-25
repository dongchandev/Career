package com.hackathon.career.domain.test.repository;

import com.hackathon.career.domain.test.entity.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {
}
