package com.tdl.tdl.repository;

import com.tdl.tdl.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    Page<Post> findByContentContaining(String info, Pageable pageable);
    List<Post> findAllByUser_UserId(Long User_id);
    Page<Post> findAllByUser_UserId(Long userId, Pageable pageable);
//    List<Post> findAllByUserId(List<Long> userIds);
}