package com.tdl.entity;


import com.tdl.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "posts")
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(name ="content",nullable = false, length = 500)
    private String content;//작성내용

    @Column(name = "likes", nullable = false)
    private int likesCount=0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    @OrderBy("commentId desc")
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostImage> postImageList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostLike> postLikesList = new ArrayList<>();

    public Post(PostRequestDto requestDto, User user) {
        this.content = requestDto.getContents();
        this.user = user;
        for(String postImage : requestDto.getPostImageList()) {
            postImageList.add(new PostImage(postImage));
        }
    }

    public void update(PostRequestDto requestDto){
        this.content = requestDto.getContents();
        postImageList = new ArrayList<>();
        for(String postImage : requestDto.getPostImageList()) {
            postImageList.add(new PostImage(postImage));
        }
    }

    public void increseLikesCount(){
        this.likesCount++;
    }

    public void decreseLikesCount(){
        this.likesCount--;
    }
}
