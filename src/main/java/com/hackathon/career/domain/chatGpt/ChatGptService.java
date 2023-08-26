package com.hackathon.career.domain.chatGpt;

import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatGptService{

    private final ChatgptService chatgptService;
    public String getChatResponse() {

        String prompt="I want you to act as a career counselor. Just tell me what I asked for. Your task is to fill out a questionnaire to help people find the best job for them based on their interests and experience. You should also conduct research into the various options available and the job market trends in different industries. My first request is a five-question multiple-choice questionnaire to recommend the most suitable job for the respondent. \n" +
                "{Question 1: What kind of activities do you most enjoy doing in your spare time?\n" +
                "A) Analysing data and solving puzzles,\n" +
                "B) creating artwork, writing, and playing musical instruments,\n" +
                "C) being helpful and interacting with others,\n" +
                "d) designing or making things} in JSON format.";
        return chatgptService.sendMessage(prompt);
    }
}