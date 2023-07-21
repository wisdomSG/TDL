package com.tdl.tdl.repository;

import com.tdl.tdl.entity.DayOfTheWeekPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DayOfTheWeekPostRepository extends JpaRepository<DayOfTheWeekPost , Long> {


    List<DayOfTheWeekPost> findAllByOrderByCreatedAtDesc();
}
