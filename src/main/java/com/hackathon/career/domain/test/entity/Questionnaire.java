package com.hackathon.career.domain.test.entity;

import com.hackathon.career.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
public class Questionnaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "json")
    private String description;
    private String job;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Questionnaire(String description, String job, User user) {
        this.description = description;
        this.job = job;
        this.user = user; // User 엔티티와 연결
    }
}
