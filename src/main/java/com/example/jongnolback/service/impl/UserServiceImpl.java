package com.example.jongnolback.service.impl;

import com.example.jongnolback.dto.UserDTO;
import com.example.jongnolback.entity.User;
import com.example.jongnolback.jwt.JwtTokenProvider;
import com.example.jongnolback.repository.UserRepository;
import com.example.jongnolback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO join(UserDTO userDTO) {
        userDTO.setProfileImg(31);
        User user = userRepository.save(userDTO.toEntity());
        return user.toDTO();
    }

    @Override
    public UserDTO login(UserDTO userDTO) {
        Optional<User> loginUser = userRepository.findByUserName(userDTO.getUserName());

        if(loginUser.isEmpty()) {
            throw new RuntimeException("not exist userid");
        }

        if(!passwordEncoder.matches(userDTO.getUserPw(), loginUser.get().getUserPw())) {
            throw new RuntimeException("wrong password");
        }

        UserDTO loginUserDTO = loginUser.get().toDTO();

        loginUserDTO.setToken(jwtTokenProvider.create(loginUser.get()));

        userRepository.save(loginUserDTO.toEntity());
        userRepository.flush();

        return loginUserDTO;
    }

    @Override
    public long idCheck(UserDTO userDTO) {
        return userRepository.countByUserName(userDTO.getUserName());
    }

    @Override
    public long nicknameCheck(UserDTO userDTO) {
        return userRepository.countByUserNickName(userDTO.getUserNickName());
    }

    @Override
    public long getUserCount() {
        return userRepository.count();
    }

    @Override
    public void updateProfile(UserDTO user, UserDTO userDTO) {
        user.setUserNickName(userDTO.getUserNickName());
        user.setProfileImg(userDTO.getProfileImg());
        userRepository.save(user.toEntity());
    }
}
