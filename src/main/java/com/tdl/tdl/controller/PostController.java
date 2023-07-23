package com.tdl.tdl.controller;


import com.tdl.tdl.dto.PostRequestDto;
import com.tdl.tdl.dto.PostResponseDto;
import com.tdl.tdl.entity.Post;
import com.tdl.tdl.entity.User;
import com.tdl.tdl.security.UserDetailsImpl;
import com.tdl.tdl.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/tdl/post")
@RequiredArgsConstructor
@Slf4j(topic = "Post")
public class PostController {

    private final PostService postService;

    // 게시물 조회
    @GetMapping("")
    public String getPosts(Model model) {
        List<PostResponseDto> post = postService.getPosts();
        model.addAttribute("postList", post);
        return "main";
    }

    @GetMapping("/add")
    public String addPost() {
        return "addPost";
    }

    // 게시물 등록
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String createPost(@ModelAttribute PostRequestDto requestDto,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        log.info(requestDto.getContents());
        for (int i = 0; i < requestDto.getMultipartFiles().size(); i++) {
            log.info(String.valueOf(requestDto.getMultipartFiles().get(i)));
        }
        postService.createPost(requestDto, userDetails.getUser());
        return "main";
    }

    // 게시물 선택 조회
    @GetMapping("/{id}")
    public String getSelectPost(@PathVariable Long id, Model model) {
        PostResponseDto post = postService.getSelectPost(id);
        model.addAttribute("post", post);
        return "postDetail";
    }

    @ResponseBody
    @GetMapping("/modify/{id}")
    public String modifyPost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PostResponseDto post = postService.getSelectPost(id);
        log.info("여기오류>?");
        log.info(userDetails.getUser().getUsername());
        if (!Objects.equals(userDetails.getUser().getUsername(), post.getUser().getUsername())) {
            log.info("오류");
            throw new IllegalArgumentException("수정불가");
        }
        return "성공";
    }

    @GetMapping("/update/{id}")
    public String updatePost(@PathVariable Long id, Model model) {
        PostResponseDto post = postService.getSelectPost(id);
        log.info(post.getContent());
        log.info(String.valueOf(post.getPostImageList().size()));
        model.addAttribute("post", post);
        return "modifyPost";
    }

    // 게시물 수정
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String updatePost(
            @ModelAttribute PostRequestDto requestDto, @PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.updatePost(id, requestDto, userDetails.getUser());
        return "main";
    }

    // 게시물 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    public String deletePost(
            @PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(id, userDetails.getUser());
    }

    // 게시물 좋아요
    @PostMapping("/like/{id}")
    @ResponseBody
    public String likePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.likePost(id, userDetails.getUser());
    }

    // 게시물 좋아요 취소
    @DeleteMapping("/like/{id}")
    @ResponseBody
    public String deleteLikePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deleteLikePost(id, userDetails.getUser());
    }
}
