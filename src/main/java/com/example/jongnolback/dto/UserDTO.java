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
    private String userId;
    private String userPw;
    private String userNickName;
    private String userRegdate;
    private String curUserPw;
    private String role;
    private String token;

    public User toEntity() {
        return User.builder()
                .id(this.id)
                .userId(this.userId)
                .userPw(this.userPw)
                .userNickName(this.userNickName)
                .userRegdate(LocalDateTime.parse(this.userRegdate))
                .build();
    }
}
