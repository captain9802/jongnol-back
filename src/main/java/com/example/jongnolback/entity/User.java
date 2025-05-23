package com.example.jongnolback.entity;

import com.example.jongnolback.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "JN_USER")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;
    @Column(unique = true)
    private String userName;
    private String userPw;
    private String userNickName;
    private LocalDateTime userRegdate;
    private String role;
    private long profileImg;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quiz> quizzes;

    public UserDTO toDTO() {
        return UserDTO.builder()
                .id(this.id)
                .userName(this.userName)
                .userPw(this.userPw)
                .userNickName(this.userNickName)
                .userRegdate(this.userRegdate.toString())
                .profileImg(this.profileImg)
                .build();
    }
}
