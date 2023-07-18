package com.tdl.service;

import com.tdl.entity.*;
import com.tdl.dto.CommentRequestDto;
import com.tdl.dto.CommentResponseDto;
import com.tdl.repository.CommentLikeRepository;
import com.tdl.repository.CommentRepository;
import com.tdl.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final MessageSource messageSource;

    @Transactional
    public CommentResponseDto addComment(Long postId, CommentRequestDto requestDto, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NullPointerException(messageSource.getMessage(
                        "not.exist.post",
                        null,
                        "해당 게시물이 존재하지 않습니다",
                        Locale.getDefault()
                ))
        );
        Comment comment = commentRepository.save(new Comment(requestDto, user, post));

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto addToComment(Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException(messageSource.getMessage(
                        "not.exist.post",
                        null,
                        "해당 댓글이 존재하지 않습니다",
                        Locale.getDefault()
                ))
        );

        Comment toComment = commentRepository.save(new Comment(requestDto, user, comment));

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = findComment(commentId);
        if(!confirmUser(comment, user)){
            throw new IllegalArgumentException(messageSource.getMessage(
                    "not.your.comment",
                    null,
                    "작성자만 수정이 가능합니다",
                    Locale.getDefault()
            ));
        }
        comment.update(requestDto);
        return new CommentResponseDto(comment);
    }

    @Transactional
    public String deleteComment(Long commentId, User user) {
        Comment comment = findComment(commentId);

        if(!confirmUser(comment, user)){
            throw new IllegalArgumentException(messageSource.getMessage(
                    "not.your.post",
                    null,
                    "작성자만 삭제가 가능합니다",
                    Locale.getDefault()
            ));
        }

        commentRepository.deleteAllByParentId(commentId);
        commentRepository.delete(comment);
        String msg ="삭제 완료";
        return msg;
    }

    @Transactional
    public String likeComment(Long id, User user) {
        Comment comment = findComment(id);
        if(commentLikeRepository.findByUserAndComment(user, comment).isPresent()){
            throw new IllegalArgumentException(messageSource.getMessage(
                    "already.like",
                    null,
                    "이미 좋아요 되어 있습니다",
                    Locale.getDefault()
            ));
        }
        CommentLike commentLike = commentLikeRepository.save(new CommentLike(user, comment));
        comment.increseLikesCount();
        String msg ="좋아요 완료";
        return msg;
    }

    @Transactional
    public String deleteLikeComment(Long id, User user) {
        Comment comment = findComment(id);
        if(commentLikeRepository.findByUserAndComment(user, comment).isEmpty()){
            throw new IllegalArgumentException(messageSource.getMessage(
                    "already.delete.like",
                    null,
                    "이미 좋아요 되어 있지 않습니다",
                    Locale.getDefault()
            ));
        }
        Optional<CommentLike> like = commentLikeRepository.findByUserAndComment(user, comment);
        commentLikeRepository.delete(like.get());
        comment.decreseLikesCount();
        String msg ="좋아요 취소";
        return msg;
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException(messageSource.getMessage(
                        "not.exist.comment",
                        null,
                        "해당 댓글이 존재하지 않습니다",
                        Locale.getDefault()
                ))
        );
    }

    private boolean confirmUser(Comment comment, User user) {
        UserRoleEnum userRoleEnum = user.getRole();
        return userRoleEnum != UserRoleEnum.USER || Objects.equals(comment.getUser().getUserId(), user.getUserId());
    }

}
