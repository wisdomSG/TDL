package com.tdl.tdl.dto;

import com.tdl.tdl.entity.DayOfTheWeekPost;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminDayOfTheWeekResponse {
    private Long sun;
    private Long mon;
    private Long tue;
    private Long wed;
    private Long thu;
    private Long fri;
    private Long sat;
    private LocalDateTime createdAt;
    public AdminDayOfTheWeekResponse(DayOfTheWeekPost dayOfWeek) {
        this.sun = dayOfWeek.getSun();
        this.mon = dayOfWeek.getMon();
        this.tue = dayOfWeek.getTue();
        this.wed = dayOfWeek.getWed();
        this.thu = dayOfWeek.getThu();
        this.fri = dayOfWeek.getFri();
        this.sat = dayOfWeek.getSat();
        this.createdAt = dayOfWeek.getCreatedAt();
    }
}
