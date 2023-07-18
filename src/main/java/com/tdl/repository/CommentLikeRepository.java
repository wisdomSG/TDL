package com.tdl.repository;

import com.tdl.entity.Comment;
import com.tdl.entity.CommentLike;
import com.tdl.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByUserAndComment(User user, Comment comment);
}
