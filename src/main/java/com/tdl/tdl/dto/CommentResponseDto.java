package com.tdl.tdl.dto;

import com.tdl.tdl.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long commentId;
    private String profileName;
    private String content;
    private LocalDateTime createdAt;
    private int likeCount;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.profileName = comment.getUser().getProfilename();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.likeCount = comment.getLikesCount();
    }
}
