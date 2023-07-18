package com.tdl.tdl.controller;


import com.tdl.tdl.dto.PostRequestDto;
import com.tdl.tdl.dto.PostResponseDto;
import com.tdl.tdl.security.UserDetailsImpl;
import com.tdl.tdl.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tdl/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("")
    public List<PostResponseDto> getPosts(){
        return postService.getPosts();
    }

    @PostMapping("")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.createPost(requestDto, userDetails.getUser());
    }

    @GetMapping("/{id}")
    public PostResponseDto getSelectPost(@PathVariable Long id){
        return postService.getSelectPost(id);
    }

    //PATCH는 수정만 담당하며 리소스의 일부분만 수정할 때 사용하고, PUT은 리소스의 모든 속성을 수정하기 위해 사용
    @PutMapping("/{id}")
    public PostResponseDto updatePost (
            @PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return postService.updatePost(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/{id}")
    public String deletePost(
            @PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return postService.deletePost(id, userDetails.getUser());
    }

    @PostMapping("/like/{id}")
    public String likePost (@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.likePost(id, userDetails.getUser());
    }

    @DeleteMapping("/like/{id}")
    public String deleteLikePost (@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deleteLikePost(id, userDetails.getUser());
    }
}
