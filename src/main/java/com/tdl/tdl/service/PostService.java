package com.tdl.tdl.service;

import com.tdl.tdl.dto.PostRequestDto;
import com.tdl.tdl.dto.PostResponseDto;
import com.tdl.tdl.entity.*;
import com.tdl.tdl.repository.PostImageRepository;
import com.tdl.tdl.repository.PostLikeRepository;
import com.tdl.tdl.repository.PostRepository;
import com.tdl.tdl.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor

public class PostService {
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final MessageSource messageSource;
    private final PostLikeRepository postLikeRepository;
    private final AwsS3Service awsS3Service;
    private final UserRepository userRepository;

    // 게시물 조회
    public List<PostResponseDto> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    // 게시물 등록
    @Transactional
    public void createPost(PostRequestDto requestDto, User user) throws Exception {
        if (requestDto.getMultipartFiles() == null) {
            throw new Exception("파일 업로드 필수");
        }
        // Aws에 이미지 저장
        List<String> imgPaths = awsS3Service.uploadFile(requestDto.getMultipartFiles());

        Post post = postRepository.save(new Post(requestDto, imgPaths, user));
        user.setPostCount(user.getPostCount() + 1);
        userRepository.save(user);
        postRepository.save(post);

        List<String> imgList = new ArrayList<>();
        for (String imgUrl : imgPaths) {
            PostImage img = new PostImage(imgUrl, post);
            postImageRepository.save(img);
            imgList.add(img.getFileName());
        }

        new PostResponseDto(post);
    }

    // 게시물 선택 조회
    public PostResponseDto getSelectPost(Long id) {
        Post post = findPost(id);
        return new PostResponseDto(post);
    }

    // 게시물 수정
    @Transactional
    public void updatePost(Long id, PostRequestDto requestDto, User user) {
        Post post = findPost(id);
        confirmUser(post, user);

        // 수정 하기 전 등록된 이미지 삭제
        List<String> fileName = new ArrayList<>();
        for(PostImage postImage : post.getPostImageList()) {
            fileName.add(postImage.getFileName());
        }
        awsS3Service.deleteFiles(fileName);
        postImageRepository.deleteAll(post.getPostImageList());

        // Aws에 수정된 이미지 저장
        List<String> imgPaths = awsS3Service.uploadFile(requestDto.getMultipartFiles());

        List<String> imgList = new ArrayList<>();
        for (String imgUrl : imgPaths) {
            PostImage img = new PostImage(imgUrl, post);
            postImageRepository.save(img);
            imgList.add(img.getFileName());
        }
        post.update(requestDto, imgList);
        ResponseEntity.ok().body(new PostResponseDto(post)).getBody();
    }

    // 게시물 삭제
    @Transactional
    public String deletePost(Long id, User user) {
        Post post = findPost(id);
        confirmUser(post, user);

        // 등록된 이미지 삭제
        List<String> imgPaths = new ArrayList<>();
        for(PostImage postImage : post.getPostImageList()) {
            imgPaths.add(postImage.getFileName());
        }
        awsS3Service.deleteFiles(imgPaths);

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
