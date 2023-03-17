package com.notification.repository;

import com.notification.entity.EmailSchedulerLogNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailSchedulerLogRepository extends JpaRepository<EmailSchedulerLogNew, Long> {
}
