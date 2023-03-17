package com.notification.repository;

import com.notification.entity.SentPushNotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SentPushNotificationLogRepository extends JpaRepository<SentPushNotificationLog, Long> {
}
