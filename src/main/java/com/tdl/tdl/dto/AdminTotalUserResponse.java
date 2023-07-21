package com.tdl.tdl.dto;

import lombok.Getter;

@Getter
public class AdminTotalUserResponse {
    Long totalUser;

    public AdminTotalUserResponse(Long totalUser){
        this.totalUser = totalUser;
    }
}
