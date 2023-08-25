package com.hackathon.career.global.auth.jwt;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.career.global.exception.ExceptionResponseDto;
import com.hackathon.career.global.exception.GlobalException;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException, java.io.IOException {

        // 1. Request Header 에서 JWT 토큰 추출
        String token = resolveToken((HttpServletRequest) request);

        if (token != null) {
            try {
                // 2. validateToken 으로 토큰 유효성 검사
                jwtTokenProvider.validateToken(token);

                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (GlobalException e) {
                // 예외 처리: 유효하지 않은 토큰일 경우
                handleInvalidTokenException((HttpServletResponse) response, e);
                return; // 필터 체인 종료
            }
        }

        chain.doFilter(request, response);
    }

    private void handleInvalidTokenException(HttpServletResponse response, GlobalException e) throws IOException, java.io.IOException {
        // 예외 정보를 사용하여 ExceptionResponseDto 생성
        ExceptionResponseDto errorResponse = ExceptionResponseDto.builder()
                .status(e.getHttpStatus().value())
                .error(e.getHttpStatus())
                .message(e.getMessage())
                .build();

        // JSON 형태로 응답을 생성하여 전송
        response.setStatus(e.getHttpStatus().value());
        response.setContentType("application/json");
        response.getWriter().write(convertObjectToJson(errorResponse));
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }



    // Request Header 에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}