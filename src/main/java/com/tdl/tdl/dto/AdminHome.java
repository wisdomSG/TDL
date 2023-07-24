package com.tdl.tdl.dto;

import lombok.Getter;

import java.util.List;
@Getter
public class AdminHome {
    private AdminUserResponseDto userResponseDto;
    private List<AdminTotalUserResponse> totalUser;
    private List<AdminTotalPostResponse> totalPost;
    private List<AdminDayOfTheWeekResponse> dayOfTheWeekResponse;
    private List<AdminPostResponseDto> topThreeLike;
    private List<AdminUserResponseDto> topThreeFollowCount;
    private List<CategoryContentsResponseDto> categoryDtoList;
    public AdminHome( AdminUserResponseDto userResponseDto,List<AdminTotalUserResponse> totalUser, List<AdminTotalPostResponse> totalPost,
                     List<AdminDayOfTheWeekResponse> dayOfTheWeekResponse, List<AdminPostResponseDto> topThreeLike,
                     List<AdminUserResponseDto> topThreeFollowCount, List<CategoryContentsResponseDto> categoryDtoList) {
        this.userResponseDto = userResponseDto;
        this.totalUser = totalUser;
        this.totalPost = totalPost;
        this.dayOfTheWeekResponse = dayOfTheWeekResponse;
        this.topThreeLike = topThreeLike;
        this.topThreeFollowCount = topThreeFollowCount;
        this.categoryDtoList = categoryDtoList;
    }
}
