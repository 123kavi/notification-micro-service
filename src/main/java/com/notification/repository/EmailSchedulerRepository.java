package com.notification.repository;

import com.notification.entity.EmailScheduler;
import com.notification.enums.ScheduledStatus;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailSchedulerRepository extends JpaRepository<EmailScheduler, Long> {

    @Cacheable(key = "#jobId", value = "existsByJobId")
    boolean existsByJobId(String jobId);

    @Cacheable(key = "#jobId", value = "findFirstByJobId")
    EmailScheduler findFirstByJobId(String jobId);

    Optional<EmailScheduler> findDistinctFirstByJobId(String jobId);

    List<EmailScheduler> findAllByScheduledStatusAndActiveStatus(ScheduledStatus scheduledStatus, boolean activeStatus);
}