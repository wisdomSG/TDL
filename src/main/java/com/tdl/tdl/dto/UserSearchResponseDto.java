package com.tdl.tdl.dto;

import com.tdl.tdl.entity.User;
import lombok.Getter;

import java.util.List;

@Getter
public class UserSearchResponseDto {

    private List<String> usernameList;

    public UserSearchResponseDto(List<User> user) {
        this.usernameList = user.stream()
                .map(User::getUsername)
                .toList();
    }

}