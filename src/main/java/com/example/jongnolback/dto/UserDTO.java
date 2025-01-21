package com.example.jongnolback.dto;

import com.example.jongnolback.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class UserDTO {
    private long id;
    private String userName;
    private String userPw;
    private String userNickName;
    private String userRegdate;
    private String curUserPw;
    private String role;
    private String token;

    public User toEntity() {
        return User.builder()
                .id(this.id)
                .userName(this.userName)
                .userPw(this.userPw)
                .userNickName(this.userNickName)
                .userRegdate(LocalDateTime.parse(this.userRegdate))
                .build();
    }
}
