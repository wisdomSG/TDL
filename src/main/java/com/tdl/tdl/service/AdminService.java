package com.tdl.tdl.service;

import com.tdl.tdl.dto.AdminPostResponseDto;
import com.tdl.tdl.dto.AdminUserResponseDto;
import com.tdl.tdl.entity.Post;
import com.tdl.tdl.entity.User;
import com.tdl.tdl.entity.UserRoleEnum;
import com.tdl.tdl.repository.PostImageRepository;
import com.tdl.tdl.repository.PostRepository;
import com.tdl.tdl.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service

public class AdminService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostImageRepository postImageRepository;


    public Page<Post> getPost(int page, User user) { // post목록
        confirmAdminToken(user);
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction,"postId");
        Pageable pageable = PageRequest.of(page,4,sort);
        return postRepository.findAll(pageable);
    }

    public Page<User> getUser(int page, User user) { // user목록
        confirmAdminToken(user);
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction,"UserId");
        Pageable pageable = PageRequest.of(page,4,sort);
        return userRepository.findAll(pageable);
    }

    public AdminUserResponseDto getSelectUser(Long userId, User IsAdminUser) { // 특정 유저의 info랑 작성한 게시글 전부 반환
        confirmAdminToken(IsAdminUser);
        User user=findUserId(userId);
        return new AdminUserResponseDto(user);
    }
    public List<AdminPostResponseDto> getSelectUserPosts(Long userId) {
        List<AdminPostResponseDto> postResponseDtoList = new ArrayList<>();
        for (Post post : postRepository.findAllByUser_UserId(userId)) {
            postResponseDtoList.add(new AdminPostResponseDto(post));
        }
        return postResponseDtoList;
    }

    public String deleteSelectUser(Long userId, User IsAdminUser) { // 특정유저 삭제
        confirmAdminToken(IsAdminUser);
        User user=findUserId(userId);
        userRepository.delete(user);
        return "삭제성공";
    }
    public String deleteSelectPost(Long postId, User IsAdminUser) { // 게시글 삭제
        confirmAdminToken(IsAdminUser);
        Post post = findPostId(postId);
        postRepository.delete(post);
        return "삭제성공";
    }
    public User findUserId(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("게시글이 존재하지 않습니다")
        );
    }
    public Post findPostId(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("게시글이 존재하지 않습니다")
        );
    }
    private void confirmAdminToken(User user){
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
        } else {
            throw new IllegalArgumentException("어드민유저만 접근할수 있습니다");
        }
    }


    public Page<Post> getSearchName(int page, User IsAdminUser,String info) {
        confirmAdminToken(IsAdminUser);
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction,"postId");
        Pageable pageable = PageRequest.of(page,4,sort);
        User user =userRepository.findByUsername(info).get();
        return postRepository.findAllByUser_UserId(user.getUserId() , pageable);
    }
    @Transactional
    public Page<Post> getSearchContent(int page, User IsAdminUser,String info) {
        confirmAdminToken(IsAdminUser);
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction,"postId");
        Pageable pageable = PageRequest.of(page,4,sort);
        return postRepository.findByContentContaining(info,pageable);
    }
}
