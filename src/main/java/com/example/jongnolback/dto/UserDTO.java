package com.example.jongnolback.dto;

import com.example.jongnolback.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private long profileImg;
    private List<QuizDTO> quizzes;

    public User toEntity() {
        return User.builder()
                .id(this.id)
                .userName(this.userName)
                .userPw(this.userPw)
                .userNickName(this.userNickName)
                .userRegdate(LocalDateTime.parse(this.userRegdate))
                .profileImg(this.profileImg)
                .build();
    }
    public void updateProfile(String userNickName, long profileImg) {
        this.userNickName = userNickName;
        this.profileImg = profileImg;
    }
}
