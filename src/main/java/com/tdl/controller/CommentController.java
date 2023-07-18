package com.tdl.controller;


import com.tdl.service.CommentService;
import com.tdl.dto.CommentRequestDto;
import com.tdl.dto.CommentResponseDto;
import com.tdl.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tdl/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}")
    public CommentResponseDto addComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.addComment(postId, requestDto, userDetails.getUser());
    }

    @PostMapping("/reply/{commentId}")
    public CommentResponseDto addToComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.addToComment(commentId, requestDto, userDetails.getUser());
    }

    @PatchMapping("/{commentId}")
    public CommentResponseDto update(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(commentId, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.deleteComment(commentId, userDetails.getUser());
    }
    @PostMapping("/like/{id}")
    public String likeBoard (@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.likeComment(id, userDetails.getUser());
    }

    @DeleteMapping("/like/{id}")
    public String deleteLikeBoard (@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteLikeComment(id, userDetails.getUser());
    }
}
