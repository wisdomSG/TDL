package com.tdl.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "commentLikes")
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentLikeId;

    @ManyToOne
    @JoinColumn(name = "commentId")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public CommentLike (User user, Comment comment){
        this.user = user;
        this.comment = comment;
    }

}
