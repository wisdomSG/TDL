package com.tdl.tdl.dto;

import lombok.Getter;

import java.util.List;
@Getter
public class AdminPostOrUserResponseDto { // 카테고리에 맞게 유저 , 게시글 정보 반환
    List<AdminUserResponseDto> adminUserList;
    List<AdminPostResponseDto> adminPostList;
    PageInfo pageInfo;
    List<CategoryContentsResponseDto> categoryDtoList;
    public AdminPostOrUserResponseDto(List<AdminUserResponseDto> list, PageInfo pageInfo , List<CategoryContentsResponseDto> categoryDtoList) {
        this.adminUserList = list;
        this.pageInfo = pageInfo;
        this.categoryDtoList = categoryDtoList;
    }
    public AdminPostOrUserResponseDto(List<AdminPostResponseDto> list, PageInfo pageInfo , List<CategoryContentsResponseDto> categoryDtoList , String temp) {
        this.adminPostList = list;
        this.pageInfo = pageInfo;
        this.categoryDtoList = categoryDtoList;
    }
}
