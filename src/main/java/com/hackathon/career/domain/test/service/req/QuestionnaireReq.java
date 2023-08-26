package com.hackathon.career.domain.test.service.req;

import com.hackathon.career.domain.test.entity.Questionnaire;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionnaireReq {
    private String description;
    private String job;

    public Questionnaire toEntity() {
        return Questionnaire.builder()
                .description(description)
                .job(job)
                .build();
    }
}
