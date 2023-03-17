package com.notification.repository;

import com.notification.entity.FeatureFlag;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {

    @Cacheable(key = "#name.concat(#status)", value = "existsByNameAndStatus")
    boolean existsByNameAndStatus(String name, boolean status);

    @Cacheable(key = "#name", value = "existsByName")
    boolean existsByName(String name);
}
