package com.hackathon.career.domain.test.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "questionnaire_id")
    private Questionnaire questionnaire;


    private String text;
    private String type;
    private String options; // JSON 형태의 문자열로 저장
    private int orderNum;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "question")
    private List<Answer> answer;}