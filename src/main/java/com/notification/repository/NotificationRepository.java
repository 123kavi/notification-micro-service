package com.notification.repository;

import com.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "SELECT N.token FROM Notification AS N WHERE N.userId = :userId")
    List<String> findTokensByUserId(long userId);

    Notification findFirstByUserIdAndToken(long userId, String token);
}
