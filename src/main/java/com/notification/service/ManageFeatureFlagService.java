package com.notification.service;

import com.notification.dto.FeatureFlagsDto;

import java.util.List;

public interface ManageFeatureFlagService {

    List<FeatureFlagsDto> getAllExistingFeatureFlags();

    String saveNewFeatureFlagDetails(FeatureFlagsDto featureFlagsDto, String userName);

    String updateFeatureFlagDetails(FeatureFlagsDto featureFlagsDto, String userName);
}
