package com.hackathon.career.domain.user.service;

import com.hackathon.career.domain.user.entity.User;
import com.hackathon.career.domain.user.entity.dto.LoginRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.hackathon.career.domain.user.entity.dto.JoinRequest;
import com.hackathon.career.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public boolean checkLoginIdDuplicate(String loginId){
        return userRepository.existsByLoginId(loginId);
    }
    public boolean checkNicknameDuplicate(String nickname){
        return userRepository.existsByNickname(nickname);
    }
    public void join(JoinRequest req) {
        userRepository.save(req.toEntity(encoder.encode(req.getPassword())));
    }
    public User login(LoginRequest req){
        Optional<User> optionalUser = userRepository.findByLoginId(req.getLoginId());
        if(optionalUser.isEmpty()){
            return null;
        }

        User user = optionalUser.get();

        if(!user.getPassword().equals(req.getPassword())){
            return null;
        }

        return user;
    }
    public User getLoginUserById(Long userId) {
        if(userId == null) return null;

        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }
    public User getLoginUserByLoginId(String loginId) {
        if(loginId == null) return null;

        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }
}
