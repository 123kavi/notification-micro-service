package com.notification.repository;

import com.notification.entity.SentSmsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SentSmsLogRepository extends JpaRepository<SentSmsLog, Long> {
}
