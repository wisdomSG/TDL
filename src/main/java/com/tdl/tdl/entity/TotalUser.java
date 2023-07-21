package com.tdl.tdl.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "totalUsers")
public class TotalUser extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long TotalUserId;
    @Column(name = "totalUsers")
    private Long TotalUsers;
    public TotalUser(Long TotalUsers){
        this.TotalUsers=TotalUsers;
    }
}

