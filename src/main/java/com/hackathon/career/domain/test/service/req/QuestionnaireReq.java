package com.hackathon.career.domain.test.service.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionnaireReq {
    private String description;


    public void QuestionnaireReq(String description) {
        this.description = description;
    }
}
