package com.example.jongnolback.service;


import com.example.jongnolback.dto.UserDTO;

public interface UserService {
    UserDTO join(UserDTO userDTO);

    UserDTO login(UserDTO userDTO);

    long idCheck(UserDTO userDTO);

    long nicknameCheck(UserDTO userDTO);

    long getUserCount();
}
