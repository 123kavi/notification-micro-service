package com.notification.repository;

import com.notification.entity.Shedlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShedLockRepository extends JpaRepository<Shedlock, String> {
}
