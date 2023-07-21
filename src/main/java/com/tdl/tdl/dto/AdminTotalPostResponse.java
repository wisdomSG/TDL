package com.tdl.tdl.dto;

import lombok.Getter;

@Getter

public class AdminTotalPostResponse {
    Long totalPost;

    public AdminTotalPostResponse(Long totalPost){
        this.totalPost = totalPost;
    }

}
