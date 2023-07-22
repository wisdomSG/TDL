package com.tdl.tdl.dto;

import com.tdl.tdl.entity.Comment;
import com.tdl.tdl.entity.Post;
import com.tdl.tdl.entity.PostImage;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostResponseDto {
    private long postId;
    private String userImage;
    private String profilename;
    private String content;
    private int likesCount;
    private LocalDateTime createdAt;
    private List<PostImageResponseDto> postImageList = new ArrayList<>();
    private List<CommentResponseDto> commentList  = new ArrayList<>();

    public PostResponseDto(Post post) {
        this.postId = post.getPostId();
        this.userImage = post.getUser().getUserImage();
        this.profilename = post.getUser().getProfilename();
        this.content = post.getContent();
        this.likesCount = post.getLikesCount();
        this.createdAt = post.getCreatedAt();
        for(PostImage postImage : post.getPostImageList()) {
            postImageList.add(new PostImageResponseDto(postImage));
        }
        for(Comment comment : post.getCommentList()) {
            commentList.add(new CommentResponseDto(comment));
        }
    }
}
