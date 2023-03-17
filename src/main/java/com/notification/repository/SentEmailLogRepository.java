package com.notification.repository;

import com.notification.entity.SentEmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SentEmailLogRepository extends JpaRepository<SentEmailLog, Long> {
}
