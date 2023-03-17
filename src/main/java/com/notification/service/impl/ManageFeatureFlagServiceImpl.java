package com.notification.service.impl;

import com.notification.dto.FeatureFlagsDto;
import com.notification.entity.FeatureFlag;
import com.notification.exception.SystemWarningException;
import com.notification.repository.FeatureFlagRepository;
import com.notification.service.ManageFeatureFlagService;
import com.notification.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ManageFeatureFlagServiceImpl implements ManageFeatureFlagService {

    private final FeatureFlagRepository featureFlagRepository;
    private final CacheManager cacheManager;

    /**
     * @return java.util.List<com.cloudofgoods.notification.dto.FeatureFlagsDto>
     * Returns all the existing feature flags in the database
     */
    @Cacheable(Utility.CACHE_VAL_FIND_ALL_FEATURE_FLAGS)
    @Override
    public List<FeatureFlagsDto> getAllExistingFeatureFlags() {
        List<FeatureFlagsDto> featureFlagsDtoList = new ArrayList<>();
        List<FeatureFlag> featureFlags = featureFlagRepository.findAll();
        for (FeatureFlag featureFlag : featureFlags) {
            featureFlagsDtoList.add(new FeatureFlagsDto(featureFlag.getId(), featureFlag.getName(),
                    featureFlag.getDescription(), featureFlag.isStatus()));
        }
        return featureFlagsDtoList;
    }

    /**
     * @param featureFlagsDto : com.cloudofgoods.notification.dto.FeatureFlagsDto
     * @param userName : java.lang.String
     * @return java.lang.String
     * Saves new feature flag details in the database
     */
    @Override
    public String saveNewFeatureFlagDetails(FeatureFlagsDto featureFlagsDto, String userName) {
        validateFeatureDetails(featureFlagsDto, userName);
        boolean isFeatureFlagNameDuplicated = featureFlagRepository.existsByName(featureFlagsDto.getFeatureFlagName());
        if (isFeatureFlagNameDuplicated) {
            throw new SystemWarningException(Utility.EX_FEATURE_FLAG_NAME_ALREADY_TAKEN);
        }
        FeatureFlag featureFlag = new FeatureFlag();
        featureFlag.setName(featureFlagsDto.getFeatureFlagName());
        featureFlag.setDescription(featureFlagsDto.getFeatureFlagDescription());
        featureFlag.setStatus(featureFlagsDto.getStatus());
        featureFlag.setLastUpdatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
        featureFlag.setLastUpdatedUser(userName);
        featureFlagRepository.save(featureFlag);
        log.info(String.format(Utility.LOG_FEATURE_FLAG_DETAILS_SUCCESSFULLY_SAVED,
                featureFlagsDto.getFeatureFlagId(), featureFlagsDto.getFeatureFlagName(), featureFlagsDto.getStatus()));

        // Clears Backend Cache
        evictFindAllFeatureFlags(featureFlag.getId());
        evictExistsByName(featureFlag.getName());
        evictExistsByNameAndStatus(featureFlag.getName(), featureFlag.isStatus());
        return Utility.MSG_FEATURE_FLAG_DETAILS_SAVED_SUCCESSFULLY;
    }

    /**
     * @param featureFlagsDto : com.cloudofgoods.notification.dto.FeatureFlagsDto
     * @param userName : java.lang.String
     * @return java.lang.String
     * Updates particular feature flag details by feature flag id
     */
    @Override
    public String updateFeatureFlagDetails(FeatureFlagsDto featureFlagsDto, String userName) {
        validateFeatureDetails(featureFlagsDto, userName);
        Optional<FeatureFlag> flag = featureFlagRepository.findById(featureFlagsDto.getFeatureFlagId());
        if (flag.isPresent()) {
            FeatureFlag featureFlag = flag.get();
            featureFlag.setName(featureFlagsDto.getFeatureFlagName());
            featureFlag.setDescription(featureFlagsDto.getFeatureFlagDescription());
            featureFlag.setStatus(featureFlagsDto.getStatus());
            featureFlag.setLastUpdatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
            featureFlag.setLastUpdatedUser(userName);
            featureFlagRepository.save(featureFlag);
            log.info(String.format(Utility.LOG_FEATURE_FLAG_DETAILS_SUCCESSFULLY_UPDATED,
                    featureFlagsDto.getFeatureFlagId(), featureFlagsDto.getStatus()));

            // Clears Backend Cache
            evictFindAllFeatureFlags(featureFlag.getId());
            evictExistsByName(featureFlag.getName());
            evictExistsByNameAndStatus(featureFlag.getName(), featureFlag.isStatus());
            return Utility.MSG_FEATURE_FLAG_DETAILS_UPDATED_SUCCESSFULLY;
        } else {
            throw new SystemWarningException(Utility.EX_FEATURE_FLAG_ID_NOT_FOUND);
        }
    }

    @CacheEvict(Utility.CACHE_VAL_FIND_ALL_FEATURE_FLAGS)
    public void evictFindAllFeatureFlags(long featureFlagId) {
        Objects.requireNonNull(cacheManager.getCache(Utility.CACHE_VAL_FIND_ALL_FEATURE_FLAGS)).clear();
        log.info(String.format(Utility.LOG_BACKEND_CACHE_CLEAN_FOR_FEATURE_FLAG_ID,
                featureFlagId, Utility.CACHE_VAL_FIND_ALL_FEATURE_FLAGS));
    }

    @CacheEvict(value = Utility.CACHE_EXISTS_BY_NAME, key = "#name")
    public void evictExistsByName(String name) {
        Objects.requireNonNull(cacheManager.getCache(Utility.CACHE_EXISTS_BY_NAME)).evict(name);
    }

    @CacheEvict(value = Utility.CACHE_EXISTS_BY_NAME_AND_STATUS, key = "#name.concat(#status)")
    public void evictExistsByNameAndStatus(String name, boolean status) {
        Objects.requireNonNull(cacheManager.getCache(Utility.CACHE_EXISTS_BY_NAME_AND_STATUS))
                .evict(name.concat(String.valueOf(status)));
    }

    /**
     * @param flagsDto : com.cloudofgoods.notification.dto.FeatureFlagsDto
     * @param userName : java.lang.String
     * Validates whether provided feature flag details matches the system requirements
     */
    private void validateFeatureDetails(FeatureFlagsDto flagsDto, String userName) {
        if (null == flagsDto) {
            throw new SystemWarningException(Utility.EX_ALL_FIELDS_REQUIRED);
        }
        if (Utility.isNullOrEmpty(flagsDto.getFeatureFlagName())) {
            throw new SystemWarningException(Utility.EX_FEATURE_FLAG_NAME_REQUIRED);
        }
        if (Utility.isNullOrEmpty(flagsDto.getFeatureFlagDescription())) {
            throw new SystemWarningException(Utility.EX_FEATURE_FLAG_DESCRIPTION_REQUIRED);
        }
        if (Utility.isNullOrEmpty(userName)) {
            throw new SystemWarningException(Utility.EX_USER_NAME_REQUIRED);
        }
    }
}
