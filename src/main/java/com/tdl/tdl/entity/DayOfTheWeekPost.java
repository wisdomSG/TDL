package com.tdl.tdl.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "dayOfTheWeekPosts")
public class DayOfTheWeekPost extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dayOfTheWeekPostId;
    @Column(name = "sun")
    private Long sun;
    @Column(name = "mon")
    private Long mon;
    @Column(name = "tue")
    private Long tue;
    @Column(name = "wed")
    private Long wed;
    @Column(name = "thu")
    private Long thu;
    @Column(name = "fri")
    private Long fri;
    @Column(name = "sat")
    private Long sat;

    public DayOfTheWeekPost( Long sun, Long mon, Long tue, Long wed, Long thu, Long fri, Long sat) {
        this.sun = sun;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
    }

}
