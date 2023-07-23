package com.tdl.tdl.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AdminSelectUserResponseDto { // 유저관리에서 유저 누르면 유저 정보 유저게시글 전부 반환하거나 , 검색반환
//    AdminUserResponseDto adminUser;
    List<AdminPostResponseDto> adminPostList;
    PageInfo info;
    List<CategoryContentsResponseDto> categoryDtoList;
    AdminUserResponseDto userInfo;


//    public AdminSelectUserResponseDto(AdminUserResponseDto selectUser, PageInfo info, List<AdminPostResponseDto> selectUserPosts) {  // 유저관리에서 유저 눌렀을때
//        this.adminUser = selectUser;
//        this.adminPostList = selectUserPosts;
//        this.info = info;
//    }
    public AdminSelectUserResponseDto(List<AdminPostResponseDto> selectUserPosts , PageInfo info , List<CategoryContentsResponseDto> categoryDtoList , AdminUserResponseDto userInfo) { // 검색했을때
        this.adminPostList = selectUserPosts;
        this.info = info;
        this.categoryDtoList = categoryDtoList;
        this.userInfo =userInfo;
    }
}
