package com.tdl.tdl.service;

import com.tdl.tdl.dto.PostImageResponseDto;
import com.tdl.tdl.dto.PostRequestDto;
import com.tdl.tdl.dto.PostResponseDto;
import com.tdl.tdl.entity.*;
import com.tdl.tdl.repository.PostImageRepository;
import com.tdl.tdl.repository.PostLikeRepository;
import com.tdl.tdl.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class PostService {
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final MessageSource messageSource;
    private final PostLikeRepository postLikeRepository;

    public List<PostResponseDto> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post = postRepository.save(new Post(requestDto, user));
        for(int i=0; i<requestDto.getPostImageList().size(); i++) {
            String fileName = requestDto.getPostImageList().get(i);
            postImageRepository.save(new PostImage(fileName, post));
        }
        return new PostResponseDto(post);
    }

    public PostResponseDto getSelectPost(Long id) {
        Post post = findPost(id);
        return ResponseEntity.ok().body(new PostResponseDto(post)).getBody();
    }

    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user) {
        Post post = findPost(id);
        confirmUser(post, user);
        post.update(requestDto);

        for(int i=0; i<requestDto.getPostImageList().size(); i++) {
            String fileName = requestDto.getPostImageList().get(i);
            postImageRepository.save(new PostImage(fileName, post));
        }

        return ResponseEntity.ok().body(new PostResponseDto(post)).getBody();
    }

    public String deletePost(Long id, User user) {
        Post post = findPost(id);
        confirmUser(post, user);
        postRepository.delete(post);
        String msg ="삭제 완료";
        return msg;
    }

    @Transactional
    public String likePost(Long id, User user) {
        Post post = findPost(id);
        if(postLikeRepository.findByUserAndPost(user, post).isPresent()){
            throw new IllegalArgumentException(messageSource.getMessage(
                    "already.like",
                    null,
                    "이미 좋아요 되어 있습니다",
                    Locale.getDefault()
            ));
        }
        PostLike like = postLikeRepository.save(new PostLike(user, post));
        post.increseLikesCount();
        String msg ="좋아요 완료";
        return msg;
    }

    @Transactional
    public String deleteLikePost(Long id, User user) {
        Post post = findPost(id);
        if(postLikeRepository.findByUserAndPost(user, post).isEmpty()){
            throw new IllegalArgumentException(messageSource.getMessage(
                    "already.delete.like",
                    null,
                    "이미 좋아요 되어 있지 않습니다",
                    Locale.getDefault()
            ));
        }
        Optional<PostLike> like = postLikeRepository.findByUserAndPost(user, post);
        postLikeRepository.delete(like.get());
        post.decreseLikesCount();
        String msg ="좋아요 취소";
        return msg;
    }

    private Post findPost(Long id){
        return postRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException(messageSource.getMessage(
                        "not.exist.post",
                        null,
                        "해당 게시물이 존재하지 않습니다",
                        Locale.getDefault()
                ))
        );
    }

    private void confirmUser(Post post, User user) {
        UserRoleEnum userRoleEnum = user.getRole();
        if (userRoleEnum == UserRoleEnum.USER && !Objects.equals(post.getUser().getUserId(), user.getUserId())) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "not.your.post",
                    null,
                    "작성자만 수정 및 삭제가 가능합니다",
                    Locale.getDefault()
            ));
        }
    }
}
