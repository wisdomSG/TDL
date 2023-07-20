package com.tdl.tdl.repository;

import com.tdl.tdl.entity.TotalPost;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TotalPostRepository extends JpaRepository<TotalPost , Long>{


    List<TotalPost> findAllByOrderByCreatedAtDesc();
}
