package com.tdl.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "follows")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;

    @Column(name = "followerId")
    private Long followerId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

}
