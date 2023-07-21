package com.tdl.tdl.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "totalPosts")
public class TotalPost extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long totalPostId;
    @Column(name = "totalPosts")
    private Long totalPosts;


    public TotalPost(Long TotalPost){
        this.totalPosts=TotalPost;
    }
}
