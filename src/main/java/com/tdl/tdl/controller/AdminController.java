package com.tdl.tdl.controller;

import com.tdl.tdl.dto.*;
import com.tdl.tdl.entity.Post;
import com.tdl.tdl.entity.User;
import com.tdl.tdl.security.UserDetailsImpl;
import com.tdl.tdl.service.AdminService;
import com.tdl.tdl.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tdl/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final CategoryService categoryService;

    @GetMapping("/") // 관리자  게시글관리 or 회원관리 조회
    public AdminPostOrUserResponseDto getCategoryBoards(@RequestParam("category")String category,
                                                        @RequestParam("page")int page,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails)  {
        if (category.equals("게시글관리")){
            Page<Post> pagePost = adminService.getPost(page-1 , userDetails.getUser());
            PageInfo pageInfo = new PageInfo((int)pagePost.getTotalElements(),page);
            List<AdminPostResponseDto> list=pagePost.map(AdminPostResponseDto::new).getContent();
            List<CategoryContentsResponseDto> categoryDtoList = categoryService.getCategorys();
            AdminUserResponseDto userInfo = adminService.getAdminUserInfo(userDetails.getUser());
            return new AdminPostOrUserResponseDto(list,pageInfo,categoryDtoList,userInfo,"."); // categoryDtoList 존재하는 카테고리도 같이 반환


        } else if (category.equals("회원관리")) {
            Page<User> pageUser = adminService.getUser(page-1 , userDetails.getUser());
            PageInfo pageInfo = new PageInfo((int)pageUser.getTotalElements(),page);
            List<AdminUserResponseDto> list=pageUser.map(AdminUserResponseDto::new).getContent();
            List<CategoryContentsResponseDto> categoryDtoList = categoryService.getCategorys();
            AdminUserResponseDto userInfo = adminService.getAdminUserInfo(userDetails.getUser());
            return new AdminPostOrUserResponseDto(list,pageInfo,categoryDtoList,userInfo);
        }
        return null;
    }

    @GetMapping("/user/{user_id}") // 특정 유저의 게시글 조회
    public AdminSelectUserResponseDto getSelectUser(@PathVariable String user_id,
                                                    @RequestParam("page")int page,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails){
        Page<Post> pagePost = adminService.getSearchName(page - 1, userDetails.getUser(), user_id);
        PageInfo pageInfo = new PageInfo((int) pagePost.getTotalElements(), page);
        List<AdminPostResponseDto> list = pagePost.map(AdminPostResponseDto::new).getContent();
        List<CategoryContentsResponseDto> categoryDtoList = categoryService.getCategorys();
        AdminUserResponseDto userInfo = adminService.getAdminUserInfo(userDetails.getUser());

        return new AdminSelectUserResponseDto(list, pageInfo, categoryDtoList,userInfo);
    }

    @DeleteMapping("/user/{user_id}") // 특정 유저 삭제
    public String deleteSelectUser(@PathVariable Long user_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return adminService.deleteSelectUser(user_id,userDetails.getUser());
    }
    @DeleteMapping("/post/{post_id}") // 특정 게시글 삭제
    public String deleteSelectPost(@PathVariable Long post_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return adminService.deleteSelectPost(post_id,userDetails.getUser());
    }

    @GetMapping("/search") // info 에 이름 입력하면 이름의 게시글 출력 이름이 없으면 null
    public AdminSelectUserResponseDto searchAll(@AuthenticationPrincipal UserDetailsImpl userDetails,
                            @RequestParam("page")int page,
                            @RequestParam("option")String option,
                            @RequestParam("info")String info) {
        if (option.equals("이름")) {
            Page<Post> pagePost = adminService.getSearchName(page - 1, userDetails.getUser(), info);
            PageInfo pageInfo = new PageInfo((int) pagePost.getTotalElements(), page);
            List<AdminPostResponseDto> list = pagePost.map(AdminPostResponseDto::new).getContent();
            List<CategoryContentsResponseDto> categoryDtoList = categoryService.getCategorys();
            AdminUserResponseDto userInfo = adminService.getAdminUserInfo(userDetails.getUser());
            return new AdminSelectUserResponseDto(list, pageInfo, categoryDtoList,userInfo);


        } else if (option.equals("내용")) { // info 에 글 내용 입력하면 일치하는 글 다 나옴 ->
            Page<Post> pagePost = adminService.getSearchContent(page - 1, userDetails.getUser(), info);
            PageInfo pageInfo = new PageInfo((int) pagePost.getTotalElements(), page);
            List<AdminPostResponseDto> list = pagePost.map(AdminPostResponseDto::new).getContent();
            List<CategoryContentsResponseDto> categoryDtoList = categoryService.getCategorys();
            AdminUserResponseDto userInfo = adminService.getAdminUserInfo(userDetails.getUser());

            return new AdminSelectUserResponseDto(list, pageInfo, categoryDtoList,userInfo);

        }
        return null;
    }

    @GetMapping("/home")
    public AdminHome AdminHome(@AuthenticationPrincipal UserDetailsImpl userDetails){
        AdminUserResponseDto userResponseDto = adminService.getAdminUserInfo(userDetails.getUser());
        List<AdminTotalUserResponse> totalUser=adminService.getTotalUser();
        List<AdminTotalPostResponse> totalPost=adminService.getTotalPost();
        List<AdminDayOfTheWeekResponse> DayOfTheWeekResponse =adminService.getDayOfTheWeek();
        List<AdminPostResponseDto> TopThreeLike = adminService.getTopThreeLike();
        List<AdminUserResponseDto> TopThreeFollowCount = adminService.getTopThreeFollowCount();
        List<CategoryContentsResponseDto> categoryDtoList = categoryService.getCategorys();
        return new AdminHome(userResponseDto,totalUser,totalPost,DayOfTheWeekResponse,TopThreeLike,TopThreeFollowCount,categoryDtoList);

    }
}
