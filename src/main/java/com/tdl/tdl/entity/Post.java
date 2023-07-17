package com.tdl.tdl.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(name ="content",nullable = false, length = 500)
    private String content;//작성내용

    @OneToMany(mappedBy = "post", cascade = {CascadeType.REMOVE})
    private List<PostImage> postImageList = new ArrayList<>();
}
