package com.example.delibuddy.domain.notification;

import com.example.delibuddy.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query
    List<Notification> findByCommentId(Long commentId);

    @Query
    List<Notification> findByUser(User user);

}
