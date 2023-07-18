package com.tdl.dto;

import com.tdl.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private String profileName;
    private String content;
    private LocalDateTime createdAt;
    private int likeCount;

    public CommentResponseDto(Comment comment) {
        this.profileName = comment.getUser().getProfilename();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.likeCount = comment.getLikesCount();
    }
}
