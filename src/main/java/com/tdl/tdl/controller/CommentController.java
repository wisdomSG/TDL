package com.tdl.tdl.controller;


import com.tdl.tdl.dto.CommentRequestDto;
import com.tdl.tdl.dto.CommentResponseDto;
import com.tdl.tdl.security.UserDetailsImpl;
import com.tdl.tdl.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tdl/comment")
@RequiredArgsConstructor
@Slf4j(topic = "CommentService")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}")
    public String addComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("들어옴");
        commentService.addComment(postId, requestDto, userDetails.getUser());
        return "redirect:/tdl/post/"+postId;
    }

    @ResponseBody
    @PostMapping("/reply/{commentId}")
    public CommentResponseDto addToComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.addToComment(commentId, requestDto, userDetails.getUser());
    }

    @ResponseBody
    @PatchMapping("/{commentId}")
    public CommentResponseDto update(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(commentId, requestDto, userDetails.getUser());
    }

    @ResponseBody
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.deleteComment(commentId, userDetails.getUser());
    }

    @ResponseBody
    @PostMapping("/like/{id}")
    public String likeBoard (@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.likeComment(id, userDetails.getUser());
    }

    @ResponseBody
    @DeleteMapping("/like/{id}")
    public String deleteLikeBoard (@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteLikeComment(id, userDetails.getUser());
    }
}
