package com.tdl.tdl.entity;


import com.tdl.tdl.dto.PostImageResponseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "postImages")
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column(name = "fileName")
    private String fileName;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    public PostImage(String fileName) {
        this.fileName = fileName;
    }

    public PostImage(String fileName, Post post) {
        this.fileName = fileName;
        this.post = post;
    }
}
