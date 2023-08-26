package com.hackathon.career.domain.chatGpt;

import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatGptService{

    private final ChatgptService chatgptService;

    public String getChatResponse() {

        String prompt="''라는 정보를 기반으로 '그렇다', '보통이다', '그렇지않다'로 답을 할수 있는 " +
                "결과값이 'AI/로봇', 'IT/SW', '게임', '공학', '교육'," +
                "'금융', '동물', '디자인', '미용/패션', '방송', '법/수사', '사회복지', '스포츠'," +
                "'여행', '영화/드라마', '우주/항공', '음식', '음악', '의료/바이오'," +
                "'환경/생태’중 하나가 되도록 직무적성평가를 위한 50문항의 질문지를 만들어서 '번호 : 질문내용'과" +
                "같이 출력해줘";
        // ChatGPT 에게 질문을 던집니다.
        return chatgptService.sendMessage(prompt);
    }
}