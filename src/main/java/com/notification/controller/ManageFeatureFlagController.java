package com.notification.controller;

import com.notification.dto.FeatureFlagsDto;
import com.notification.service.ManageFeatureFlagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v5/manage-feature-flag")
public class ManageFeatureFlagController {

    private final ManageFeatureFlagService featureFlagsService;

    @GetMapping(value = "/feature-flags", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FeatureFlagsDto>> getAllExistingFeatureFlags() {
        return ResponseEntity.ok(featureFlagsService.getAllExistingFeatureFlags());
    }

    @PostMapping(value = "/save-feature-flag", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveFeatureFlagDetails(@RequestBody FeatureFlagsDto featureFlagsDto) {
                                                                    // , Authentication authentication ) {
//        return ResponseEntity.ok(featureFlagsService.saveNewFeatureFlagDetails(featureFlagsDto, authentication.getName()));
        return ResponseEntity.ok(featureFlagsService.saveNewFeatureFlagDetails(featureFlagsDto, "SYSTEM"));
    }

    @PostMapping("/update-feature-flag")
    public ResponseEntity<String> updateFeatureFlagDetails(@RequestBody FeatureFlagsDto featureFlagsDto) {
                                                                    // , Authentication authentication ) {
//        return ResponseEntity.ok(featureFlagsService.updateFeatureFlagDetails(featureFlagsDto, authentication.getName())); // TODO - Uncomment after enable security
        return ResponseEntity.ok(featureFlagsService.updateFeatureFlagDetails(featureFlagsDto, "SYSTEM")); // TODO - Remove after enable security
    }
}
