package com.tdl.tdl.dto;

import com.tdl.tdl.entity.Post;
import lombok.Getter;

@Getter
public class AdminPostResponseDto {
    private Long id;
    private String content;
    private String Username;
    private int likeCount;

    public AdminPostResponseDto(Post post) {
        this.id = post.getPostId();
        this.content = post.getContent();
        this.Username = post.getUser().getUsername();
        this.likeCount = post.getLikesCount();

    }
}
