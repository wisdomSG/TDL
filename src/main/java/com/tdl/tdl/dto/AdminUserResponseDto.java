package com.tdl.tdl.dto;

import com.tdl.tdl.entity.User;
import lombok.Getter;
@Getter
public class AdminUserResponseDto {
    private Long id;
    private String username;
    private String userImage;
    private Long followersCount;


    public AdminUserResponseDto(User user) {
        this.id = user.getUserId();
        this.username = user.getUsername();
        this.userImage = user.getUserImage();
        this.followersCount = user.getFollowersCount();
    }

}
