package com.notification.repository;

import com.notification.entity.EmailSchedulerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailSchedulerDetailRepository extends JpaRepository<EmailSchedulerDetail, Long> {

    List<EmailSchedulerDetail> findAllByEmailScheduler_JobId(String jobId);
}