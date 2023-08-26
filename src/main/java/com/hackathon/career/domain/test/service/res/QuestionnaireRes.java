package com.hackathon.career.domain.test.service.res;

import com.hackathon.career.domain.test.entity.Questionnaire;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class QuestionnaireRes {
    private String username;
    private String job;

    @Builder
    public QuestionnaireRes(String job, String username) {
        this.username=username;
        this.job=job;
    }
    public QuestionnaireRes(Questionnaire questionnaire) {
        this.username=questionnaire.getUser().getUsername();
        this.job=questionnaire.getJob();
    }
}
