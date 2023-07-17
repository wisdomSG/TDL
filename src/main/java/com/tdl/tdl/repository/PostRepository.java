package com.tdl.tdl.repository;

import com.tdl.tdl.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}