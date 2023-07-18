package com.tdl.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
