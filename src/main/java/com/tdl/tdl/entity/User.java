package com.tdl.tdl.entity;


import com.tdl.tdl.dto.UserProfileRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Table(name = "users")
public class User {

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

    @OneToMany(mappedBy = "user")
    private List<Post> postList = new ArrayList<>();

    @ColumnDefault("0")
    @Column(name = "follow_count", nullable = false)
    private Long followCount;

    @ColumnDefault("0")
    @Column(name = "follower_count", nullable = false)
    private Long followerCount;

    @Column
    @Enumerated(value = EnumType.STRING) // enum 타입을 데이터베이스에 저장할때 사용하는 애너테이션
    private UserRoleEnum role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Post> post = new ArrayList<>();

    @ColumnDefault("0")
    @Column(name = "follow_count", nullable = false)
    private Long followCount;

    @ColumnDefault("0")
    @Column(name = "follower_count", nullable = false)
    private Long followerCount;

    public User(String username, String password, String profilename, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.profilename = profilename;
        this.role = role;
    }

    public void update(UserProfileRequestDto userProfileRequestDto, String userImage, String password) {
        this.userImage = userImage;
        this.profilename = userProfileRequestDto.getProfileName();
        this.introduction = userProfileRequestDto.getIntroduction();
        this.phoneNumber = userProfileRequestDto.getPhoneNumber();
        this.password = password;
    }

}

