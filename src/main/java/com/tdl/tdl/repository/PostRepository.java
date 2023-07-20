package com.tdl.tdl.repository;

import com.tdl.tdl.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    Page<Post> findByContentContaining(String info, Pageable pageable);
    List<Post> findAllByUser_UserId(Long User_id);
    Page<Post> findAllByUser_UserId(Long userId, Pageable pageable);

    @Query(value = "SELECT count(ac) as count, function('DATE_FORMAT', ac.createdAt , '%Y-%m-%d') as date " +
            "FROM Post ac " +
            "WHERE ac.createdAt " +
            "BETWEEN :startDate AND :endDate " +
            "GROUP BY function('DATE_FORMAT', ac.createdAt, '%Y-%m-%d')")
    public List<Map<String,Object>> findDayOfWeek(@Param("startDate") LocalDateTime startDate, @Param("endDate")LocalDateTime  endDate);


    List<Post> findAllByOrderByLikesCountDesc();
}