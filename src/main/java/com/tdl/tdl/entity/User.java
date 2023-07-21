package com.tdl.tdl.entity;


import com.tdl.tdl.dto.UserProfileRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "profilename")
    private String profilename;

    @ColumnDefault("'default.png'")
    @Column(name = "userImage", nullable = false)
    private String userImage;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "phoneNumber")
    private String phoneNumber;


    @OneToMany(mappedBy = "user",  cascade = CascadeType.REMOVE)
    private List<Post> postList = new ArrayList<>();

    @ColumnDefault("0")
    @Column(name = "followersCount", nullable = false)
    private Long followersCount;

    @ColumnDefault("0")
    @Column(name = "followingCount", nullable = false)
    private Long followingCount;

    @Column
    @Enumerated(value = EnumType.STRING) // enum 타입을 데이터베이스에 저장할때 사용하는 애너테이션
    private UserRoleEnum role;


    private Long kakaoId;

    public User(String username, String password, String profilename, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.profilename = profilename;
        this.role = role;
    }


    public User(String username, String password, String profilename, UserRoleEnum role, Long kakaoId) {
        this.username = username;
        this.password = password;
        this.profilename = profilename;
        this.role = role;
        this.kakaoId = kakaoId;
    }

    public void update(UserProfileRequestDto userProfileRequestDto, String userImage, String password) {
        this.userImage = userImage;
        this.profilename = userProfileRequestDto.getProfileName();
        this.introduction = userProfileRequestDto.getIntroduction();
        this.phoneNumber = userProfileRequestDto.getPhoneNumber();
        this.password = password;
        this.kakaoId = kakaoId;
    }

    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }
}

