package com.tdl.repository;

import com.tdl.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteAllByParentId(Long commentId);
}
