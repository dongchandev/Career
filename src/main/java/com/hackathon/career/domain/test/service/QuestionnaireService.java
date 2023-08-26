package com.hackathon.career.domain.test.service;

import com.hackathon.career.domain.test.entity.Questionnaire;
import com.hackathon.career.domain.test.repository.QuestionnaireRepository;
import com.hackathon.career.domain.test.service.req.QuestionnaireReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionnaireService {
    private QuestionnaireRepository questionnaireRepository;

    public Questionnaire createQuestionnaire(QuestionnaireReq req) {
        return questionnaireRepository.save(req.toEntity());
    }
}
