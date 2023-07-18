package com.tdl.dto;

import lombok.Getter;

@Getter
public class UserProfileRequestDto {
    private String userImage;
    private String profileName;
    private String introduction;
    private String phoneNumber;
    private String password;
}
