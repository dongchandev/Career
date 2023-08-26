package com.hackathon.career.domain.chatGpt;

import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatGptService{

    private final ChatgptService chatgptService;
    public String getChatResponse() {

        String prompt="I want you to act as a career counselor. Just tell me what I asked for. Your task is to fill out a questionnaire to help people find the best job for them based on their interests and experience. You should also conduct research into the various options available and the job market trends in different industries. My first request is \"Please provide a 5-question multiple-choice questionnaire in Korean in JSON format to recommend the best job for the respondent.\"";


            return chatgptService.sendMessage(prompt);
    }
}