package com.hackathon.career.domain.test.controller;

import com.hackathon.career.domain.chatGpt.ChatGptService;
import com.hackathon.career.domain.test.entity.Questionnaire;
import com.hackathon.career.domain.test.service.QuestionnaireService;
import com.hackathon.career.domain.test.service.req.QuestionnaireReq;
import com.hackathon.career.domain.test.service.res.QuestionnaireRes;
import com.hackathon.career.domain.user.entity.User;
import com.hackathon.career.domain.user.service.UserService;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class QuestionnaireController {
    private QuestionnaireService questionnaireService;
    private final ChatGptService chatGptService;
    private final ChatgptService chatgptService;
    private final UserService userService;

    @GetMapping("/question")
    public String test() {
        return chatGptService.getChatResponse();
    }

    @PostMapping("/question")
    public ResponseEntity<Questionnaire> createQuestionnaire(@RequestBody QuestionnaireReq req) {
        Questionnaire createdQuestionnaire = questionnaireService.createQuestionnaire(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestionnaire);
    }

    @GetMapping("/result")
    public ResponseEntity<QuestionnaireRes> result() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 현재 로그인한 사용자 정보 가져오기
        User currentUser = userService.getUserByUsername(username);

        // 유저의 job 정보 가져오기
        String job = currentUser.getJob();

        // QuestionnaireRes 객체 생성 및 설정
        QuestionnaireRes response = new QuestionnaireRes();
        response.setUsername(username);
        response.setJob(job);

        return ResponseEntity.ok(response);
    }
}
