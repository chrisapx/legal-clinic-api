package org.lc.kwengineadapter.identity_service.repository;

import org.lc.kwengineadapter.identity_service.entity.User;
import org.lc.kwengineadapter.identity_service.entity.UserActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    Page<UserActivity> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    List<UserActivity> findTop20ByUserOrderByCreatedAtDesc(User user);
    Page<UserActivity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
