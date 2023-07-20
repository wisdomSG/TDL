package com.tdl.tdl.repository;

import com.tdl.tdl.entity.TotalUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TotalUserRepository extends JpaRepository<TotalUser , Long> {

    List<TotalUser> findAllByOrderByCreatedAtDesc();
}
