package com.tdl.tdl.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchRequestDto {
    private String keyword;

    public UserSearchRequestDto(String keyword) {
        this.keyword = keyword;
    }
}
