package com.tdl.tdl.repository;

import com.tdl.tdl.entity.Post;
import com.tdl.tdl.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
