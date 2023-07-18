package com.tdl.tdl.repository;

import com.tdl.tdl.entity.Comment;
import com.tdl.tdl.entity.CommentLike;
import com.tdl.tdl.entity.PostLike;
import com.tdl.tdl.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByUserAndComment(User user, Comment comment);
}
