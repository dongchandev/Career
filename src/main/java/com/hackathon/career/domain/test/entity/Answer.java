package com.hackathon.career.domain.test.entity;

import jakarta.persistence.*;

@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    // 사용자 식별자, 답변 내용 등의 필드 추가
    // Getters, Setters, Constructors
}