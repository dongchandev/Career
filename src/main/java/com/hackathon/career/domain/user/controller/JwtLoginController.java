package com.hackathon.career.domain.user.controller;

import com.hackathon.career.domain.user.entity.dto.JoinRequest;
import com.hackathon.career.domain.user.entity.dto.LoginRequest;
import com.hackathon.career.domain.user.service.UserService;
import com.hackathon.career.global.auth.jwt.TokenInfo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt")
public class JwtLoginController {
    private final UserService userService;

    @PostMapping("/join")
    public String join(@RequestBody JoinRequest joinRequest) {

        // loginId 중복 체크
        if(userService.checkLoginIdDuplicate(joinRequest.getLoginId())) {
            return "로그인 아이디가 중복됩니다.";
        }
        // 닉네임 중복 체크
        if(userService.checkUsernameDuplicate(joinRequest.getUsername())) {
            return "닉네임이 중복됩니다.";
        }

        userService.join(joinRequest);
        return "회원가입 성공";
    }
    @PostMapping("/login")
    public TokenInfo login(@RequestBody LoginRequest req, HttpServletResponse response) {
        String loginId = req.getLoginId();
        String password = req.getPassword();
        TokenInfo tokenInfo = userService.login(loginId, password,response);
        return tokenInfo;
    }
//    @PostMapping("/test")
//    public String test() {
//        return "success";
//    }
}