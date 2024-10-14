package com.example.jongnolback.entity;

import com.example.jongnolback.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


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
    private String userId;
    private String userPw;
    private String userNickName;
    private LocalDateTime userRegdate;
    private String role;

    public UserDTO toDTO() {
        return UserDTO.builder()
                .id(this.id)
                .userId(this.userId)
                .userPw(this.userPw)
                .userRegdate(this.userRegdate.toString())
                .build();
    }
}
