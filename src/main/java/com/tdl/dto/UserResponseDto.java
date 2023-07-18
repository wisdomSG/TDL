package com.tdl.dto;

import com.tdl.entity.Post;
import com.tdl.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class UserResponseDto {
    private String userImage;
    private String profileName;
    private String introduction;
    private Long followCount;
    private Long followerCount;
    private List<PostResponseDto> postList = new ArrayList<>();

    public UserResponseDto(User user) {
        this.userImage = user.getUserImage();
        this.profileName = user.getProfilename();
        this.introduction = user.getIntroduction();
        this.followCount = user.getFollowCount();
        this.followerCount = user.getFollowerCount();

        if (user.getPostList().size() > 0) {
            for (Post post : user.getPostList()) {
                this.postList.add(new PostResponseDto(post));
            }
        }
    }
}
