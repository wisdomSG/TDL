package com.tdl.tdl.repository;

import com.tdl.tdl.entity.Follow;
import com.tdl.tdl.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface FollowingRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
//    List<Follow> findAllByFollowing(User following);
    List<Follow> findByFollowId(Long id);
}
