package com.hackathon.career.domain.test.controller;

import com.hackathon.career.domain.chatGpt.ChatGptService;
import com.hackathon.career.domain.test.entity.Questionnaire;
import com.hackathon.career.domain.test.service.QuestionnaireService;
import com.hackathon.career.domain.test.service.req.QuestionnaireReq;
import com.hackathon.career.domain.test.service.res.QuestionnaireRes;
import com.hackathon.career.domain.test.service.res.ResponseData;
import com.hackathon.career.domain.user.entity.User;
import com.hackathon.career.domain.user.service.UserService;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    public ResponseEntity<String> recommendCareer(@RequestBody List<ResponseData> responses) {
        // responses 리스트를 활용하여 직업 추천 로직을 수행하는 부분

        // 예시로 직업 추천 메시지를 생성합니다.
        String recommendationMessage = generateRecommendation(responses);

        return ResponseEntity.status(HttpStatus.OK).body(recommendationMessage);
    }
    private String generateRecommendation(List<ResponseData> responses) {
        // ChatGPT API를 사용하여 추천 메시지 생성
        String prompt = "Suggest a job using "+buildPrompt(responses)+" and give me the names of 3 relevant companies and 3 areas I need to study to do the job in JSON.";
        return getChatGptResponse(prompt);
    }
    private String buildPrompt(List<ResponseData> responses) {
        StringBuilder prompt = new StringBuilder();

        for (ResponseData response : responses) {
            prompt.append("질문: ").append(response.getQuestion()).append("\n");
            prompt.append("답변: ").append(response.getAnswer()).append("\n");
        }

        return prompt.toString();
    }

    public String getChatGptResponse(String prompt) {
        return chatgptService.sendMessage(prompt);
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
