package com.notification.repository;

import com.notification.entity.ScheduleEmailRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduledEmailRepository extends JpaRepository<ScheduleEmailRequest, Long> {
    List<ScheduleEmailRequest> findAllByDateTimeGreaterThanEqualAndIsActiveEquals(LocalDateTime dateTime, boolean b);
}