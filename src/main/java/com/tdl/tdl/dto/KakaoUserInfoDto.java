package com.tdl.tdl.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {
    private Long id;
    private String profilename;
    private String username;

    public KakaoUserInfoDto(Long id, String profilename, String username) {
        this.id = id;
        this.profilename = profilename;
        this.username = username;
    }
}
