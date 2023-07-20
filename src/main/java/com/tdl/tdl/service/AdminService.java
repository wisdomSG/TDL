package com.tdl.tdl.service;

import com.tdl.tdl.dto.*;
import com.tdl.tdl.entity.*;
import com.tdl.tdl.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service

public class AdminService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TotalPostRepository totalPostRepository;
    private final TotalUserRepository totalUserRepository;
    private final DayOfTheWeekPostRepository dayOfTheWeekPostRepository;


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

    public List<AdminTotalUserResponse> getTotalUser() {
        if (totalUserRepository.findAll().size() < 2 ){
            List<AdminTotalUserResponse> totalUserResponse = new ArrayList<>();
            List<TotalUser> totalUser  = totalUserRepository.findAll();
            for (TotalUser user : totalUser) {
                totalUserResponse.add(new AdminTotalUserResponse(user.getTotalUsers()));
            }
            return totalUserResponse;
        } else {
            List<AdminTotalUserResponse> totalUserResponses = new ArrayList<>(); // 제일 최신 토탈유저데이터가 리스트에 맨앞으로 들어감
            List<TotalUser> totalUser = totalUserRepository.findAllByOrderByCreatedAtDesc();
            for (int i = 0; i < 2; i++) {
                totalUserResponses.add(new AdminTotalUserResponse(totalUser.get(i).getTotalUsers()));
            }
            return totalUserResponses;
        }
    }


    public List<AdminTotalPostResponse> getTotalPost() {
        if (totalPostRepository.findAll().size() < 2 ){
            List<AdminTotalPostResponse> totalPostResponse = new ArrayList<>();
            List<TotalPost> totalPost  = totalPostRepository.findAll();
            for (TotalPost Post : totalPost) {
                totalPostResponse.add(new AdminTotalPostResponse(Post.getTotalPosts()));
            }
            return totalPostResponse;
        } else {
            List<AdminTotalPostResponse> totalPostResponses = new ArrayList<>(); // 제일 최신 토탈유저데이터가 리스트에 맨앞으로 들어감
            List<TotalPost> totalPost = totalPostRepository.findAllByOrderByCreatedAtDesc();
            for (int i = 0; i < 2; i++) {
                totalPostResponses.add(new AdminTotalPostResponse(totalPost.get(i).getTotalPosts()));
            }
            return totalPostResponses;
        }

    }

    public List<AdminDayOfTheWeekResponse> getDayOfTheWeek() {
        if (dayOfTheWeekPostRepository.findAll().size() < 2 ){
            List<AdminDayOfTheWeekResponse> dayOfTheWeekResponse = new ArrayList<>();
            List<DayOfTheWeekPost> dayOfTheWeekPost  = dayOfTheWeekPostRepository.findAll();
            for (DayOfTheWeekPost DayOfWeek : dayOfTheWeekPost) {
                dayOfTheWeekResponse.add(new AdminDayOfTheWeekResponse(DayOfWeek));
            }
            return dayOfTheWeekResponse;
        } else {
            List<AdminDayOfTheWeekResponse> dayOfTheWeekResponses = new ArrayList<>(); // 제일 최신 토탈유저데이터가 리스트에 맨앞으로 들어감
            List<DayOfTheWeekPost> totalPost = dayOfTheWeekPostRepository.findAllByOrderByCreatedAtDesc();
            for (int i = 0; i < 2; i++) {
                dayOfTheWeekResponses.add(new AdminDayOfTheWeekResponse(totalPost.get(i)));
            }
            return dayOfTheWeekResponses;
        }
    }

    public List<AdminPostResponseDto> getTopThreeLike() {
        if (postRepository.findAllByOrderByLikesCountDesc().size() < 3  ) {
            List<AdminPostResponseDto> postResponseDto = new ArrayList<>();
            List<Post> posts = postRepository.findAllByOrderByLikesCountDesc();
            for (Post post : posts) {
                postResponseDto.add(new AdminPostResponseDto(post));
            }
            return postResponseDto;
        } else {
            List<AdminPostResponseDto> postResponses = new ArrayList<>();
            List<Post> posts = postRepository.findAllByOrderByLikesCountDesc();
            for (int i = 0; i < 3; i++) {
                postResponses.add(new AdminPostResponseDto(posts.get(i)));
            }
            return postResponses;
        }
    }

    public List<AdminUserResponseDto> getTopThreeFollowCount() {
        if (userRepository.findAllByOrderByFollowersCountDesc().size() < 3  ) {
            List<AdminUserResponseDto> userResponseDto = new ArrayList<>();
            List<User> users = userRepository.findAllByOrderByFollowersCountDesc();
            for (User user : users) {
                userResponseDto.add(new AdminUserResponseDto(user));
            }
            return userResponseDto;
        } else {
            List<AdminUserResponseDto> userResponseDtos = new ArrayList<>();
            List<User> users = userRepository.findAllByOrderByFollowersCountDesc();
            for (int i = 0; i < 3; i++) {
                userResponseDtos.add(new AdminUserResponseDto(users.get(i)));
            }
            return userResponseDtos;
        }
    }


    public AdminUserResponseDto getAdminUserInfo(User user) {
        confirmAdminToken(user);
        return new AdminUserResponseDto(user);
    }
}
