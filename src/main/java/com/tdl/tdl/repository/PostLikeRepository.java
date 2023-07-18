package com.tdl.tdl.repository;

import com.tdl.tdl.entity.Post;
import com.tdl.tdl.entity.PostLike;
import com.tdl.tdl.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);
}
