package com.tdl.entity;


import com.tdl.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "comments")
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(name = "content")
    private String content;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "likes", nullable = false)
    private int likesCount=0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<CommentLike> commentLikesList = new ArrayList<>();

    public Comment(CommentRequestDto requestDto, User user, Post post) {
        this.content = requestDto.getContent();
        this.user = user;
        this.post = post;
    }

    public Comment(CommentRequestDto requestDto, User user, Comment comment) {
        this.content = requestDto.getContent();
        this.user = user;
        this.parentId = comment.getCommentId();
        this.post = comment.getPost();
    }

    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

    public void increseLikesCount(){
        this.likesCount++;
    }

    public void decreseLikesCount(){
        this.likesCount--;
    }
}
